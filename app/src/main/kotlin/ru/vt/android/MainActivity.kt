package ru.vt.android

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import ru.vt.android.ui.CommonEventHandler
import ru.vt.android.ui.CommonLoaderHandler
import ru.vt.core.dependency.androidDeps
import ru.vt.core.dependency.sysServiceDeps
import ru.vt.core.navigation.AppNavigation
import ru.vt.core.navigation.AppNavigator
import ru.vt.core.ui.compose.LocalVibratorService
import ru.vt.core.ui.compose.collectWithLifecycle
import ru.vt.core.ui.compose.theme.AppTheme
import ru.vt.core.ui.compose.utils.noRippleClickable
import ru.vt.core.ui.feedback.FeedbackEvent
import ru.vt.core.ui.feedback.FeedbackSnackbarEvent
import ru.vt.core.ui.feedback.FeedbackToastEvent
import ru.vt.core.ui.feedback.UiHost
import java.lang.Integer.max
import kotlin.coroutines.CoroutineContext

internal class MainActivity : ComponentActivity(), UiHost, CoroutineScope {

    private val job = SupervisorJob()
    private val feedbacks = Channel<FeedbackEvent>(Channel.CONFLATED)
    private val loadingFlow: MutableStateFlow<Int> = MutableStateFlow(0)

    override val coroutineContext: CoroutineContext = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val debouncedLoading = loadingFlow.debounce(250L)

        setContent {
            CompositionLocalProvider(
                LocalVibratorService provides sysServiceDeps.vibratorService
            ) {
                ProvideWindowInsets(
                    consumeWindowInsets = false,
                    windowInsetsAnimationsEnabled = true
                ) {
                    AppTheme {
                        // Update the system bars to be translucent
                        val systemUiController = rememberSystemUiController()
                        val useDarkIcons = MaterialTheme.colors.isLight
                        SideEffect {
                            systemUiController.setSystemBarsColor(
                                Color.Transparent,
                                darkIcons = useDarkIcons
                            )
                        }

                        val snackbarHostState = remember { SnackbarHostState() }
                        HandleFeedbacks(snackbarHostState = snackbarHostState)

                        val navController = rememberNavController()
                        LaunchedEffect(key1 = navController, block = {
                            androidDeps.navigator = AppNavigator(navController = navController)
                        })

                        navController.OnDestinationChangedClearLoader()

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .navigationBarsPadding(bottom = false)
                        ) {
                            AppNavigation(navController = navController)

                            SnackbarHost(
                                hostState = snackbarHostState,
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .navigationBarsPadding()
                            )

                            val loadingState by collectWithLifecycle(
                                flow = debouncedLoading,
                                initial = 0
                            )
                            if (loadingState > 0) {
                                Box(modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        color = MaterialTheme.colors.background.copy(alpha = 0.75f)
                                    )
                                    .noRippleClickable { }) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.align(Alignment.Center),
                                        color = MaterialTheme.colors.primaryVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        androidDeps.loaderHandler = CommonLoaderHandler(this)
    }

    override fun onStart() {
        super.onStart()
        androidDeps.eventHandler = CommonEventHandler(this)
    }

    override fun onStop() {
        super.onStop()
        androidDeps.eventHandler = null
        job.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        androidDeps.loaderHandler = null
    }

    override fun handleFeedbackEvent(event: FeedbackEvent) {
        launch {
            feedbacks.send(event)
        }
    }

    override fun setLoading(loading: Boolean) {
        launch {
            val counter = max(0, loadingFlow.value + (if (loading) 1 else -1))
            loadingFlow.emit(counter)
        }
    }

    @Composable
    private fun HandleFeedbacks(snackbarHostState: SnackbarHostState) {
        LaunchedEffect(key1 = feedbacks, block = {
            feedbacks.consumeAsFlow().collect {
                when (it) {
                    is FeedbackSnackbarEvent -> snackbarHostState.showSnackbar(
                        message = it.message,
                        duration = it.duration
                    )
                    is FeedbackToastEvent -> Toast.makeText(
                        this@MainActivity,
                        it.message,
                        it.length
                    ).show()
                }
            }
        })
    }

    @Stable
    @Composable
    private fun NavController.OnDestinationChangedClearLoader() {
        DisposableEffect(key1 = this) {
            val listener = NavController.OnDestinationChangedListener { _, _, _ ->
                this@MainActivity.launch { loadingFlow.emit(0) }
            }
            addOnDestinationChangedListener(listener)

            onDispose {
                removeOnDestinationChangedListener(listener)
            }
        }
    }
}
