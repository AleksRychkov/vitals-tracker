package ru.vt.presentation.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.vt.core.common.extension.handleNullable
import ru.vt.core.dependency.androidDeps
import ru.vt.core.dependency.annotation.DependencyAccessor
import ru.vt.core.dependency.dataDeps
import ru.vt.core.navigation.contract.Navigator
import ru.vt.core.navigation.contract.SplashToDashboard
import ru.vt.core.navigation.contract.SplashToProfileCreate
import ru.vt.domain.profile.usecase.GetDefaultProfileUseCase

@OptIn(DependencyAccessor::class)
@Composable
fun ScreenSplash() {
    LaunchedEffect(key1 = true, block = {
        val navigator: Navigator = androidDeps.navigator
        launch {
            delay(500) // dunno why

            GetDefaultProfileUseCase(dataDeps.profileRepository)
                .invoke(Unit)
                .handleNullable(
                    onSuccess = {
                        it?.let { navigator.navigate(SplashToDashboard(it.id)) }
                            ?: navigator.navigate(SplashToProfileCreate)
                    },
                    eventHandler = androidDeps.eventHandler,
                    dispatcher = Dispatchers.Main
                )
                .collect()
        }
    })
}
