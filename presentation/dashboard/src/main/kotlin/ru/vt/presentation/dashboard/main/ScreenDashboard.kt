package ru.vt.presentation.dashboard.main

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.statusBarsPadding
import ru.vt.core.ui.compose.collectWithLifecycle
import ru.vt.core.ui.compose.components.AppBottomSheet
import ru.vt.core.ui.compose.components.ExpandBottomSheet
import ru.vt.core.ui.compose.theme.AppTheme
import ru.vt.core.ui.viewmodel.withFactory
import ru.vt.presentation.dashboard.main.ui.DashboardMeasurements
import ru.vt.presentation.dashboard.main.ui.EditSummary
import ru.vt.presentation.dashboard.main.ui.EmptyDashboard
import ru.vt.presentation.dashboard.main.ui.Header

@Composable
fun ScreenDashboard(profileId: Long) {
    val vm: DashboardViewModel =
        viewModel(factory = withFactory(factory = DashboardViewModel.Factory()))

    LaunchedEffect(key1 = profileId, block = {
        if (vm.hasRestoredState().not()) {
            vm.submitAction(ActionDashboard.Init(profileId = profileId))
        }
    })

    ScreenDashboard(vm = vm, initialState = vm.initialState())
}

@Composable
private fun ScreenDashboard(vm: DashboardViewModel, initialState: StateDashboard) {
    val viewState by collectWithLifecycle(flow = vm.stateFlow, initial = initialState)
    ScreenDashboard(state = derivedStateOf { viewState }.value, actionHandler = {
        vm.submitAction(it)
    })
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ScreenDashboard(state: StateDashboard, actionHandler: (ActionDashboard) -> Unit) {
    AppBottomSheet(
        sheetContent = { _, _ -> EditSummary(state = state, actionHandler = actionHandler) }
    ) { expand, _ ->
        ScreenDashboard(
            state = state,
            expandBottomSheet = expand,
            actionHandler = actionHandler
        )
    }
}

@Composable
private fun ScreenDashboard(
    state: StateDashboard,
    expandBottomSheet: ExpandBottomSheet,
    actionHandler: (ActionDashboard) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        val editTextPosition = remember { mutableStateOf<Offset?>(null) }
        Header(
            state = state,
            expandBottomSheet = expandBottomSheet,
            editTextPosition = editTextPosition
        )
        Box(modifier = Modifier.fillMaxSize()) {
            Crossfade(targetState = state.hasTrackedItems) {
                if (it) {
                    DashboardMeasurements(
                        state = state,
                        actionHandler = actionHandler
                    )
                } else {
                    EmptyDashboard(
                        editTextPosition = editTextPosition
                    )
                }
            }
        }
    }
}


@Preview("ScreenPreview", showSystemUi = true, showBackground = true)
@Composable
private fun ScreenPreview() {
    AppTheme {
        val state = StateDashboard()
        ScreenDashboard(state = state, actionHandler = {})
    }
}