package ru.vt.presentation.profile.create

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.navigationBarsWithImePadding
import kotlinx.coroutines.launch
import ru.vt.core.ui.compose.collectWithLifecycle
import ru.vt.core.ui.compose.theme.AppTheme
import ru.vt.core.ui.compose.utils.noRippleClickableConditioned
import ru.vt.core.ui.compose.utils.pickerBottomSheetWithPaddings
import ru.vt.core.ui.compose.utils.pxToDp
import ru.vt.core.ui.viewmodel.withFactory
import ru.vt.presentation.profile.create.ui.CreateProfileBottomSheetPicker
import ru.vt.presentation.profile.create.ui.CreateProfileForm
import ru.vt.presentation.profile.create.ui.PickerType

@Composable
fun ScreenCreateProfile() {
    val vm: CreateProfileViewModel =
        viewModel(factory = withFactory(factory = CreateProfileViewModel.Factory()))

    ScreenCreateProfile(vm, vm.initialState())
}

@Composable
private fun ScreenCreateProfile(vm: CreateProfileViewModel, initialState: StateCreateProfile) {
    val viewState by collectWithLifecycle(flow = vm.stateFlow, initial = initialState)

    ScreenCreateProfile(state = derivedStateOf { viewState }.value) { vm.submitAction(it) }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
private fun ScreenCreateProfile(
    state: StateCreateProfile,
    actionHandler: (ActionCreateProfile) -> Unit
) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Collapsed,
            confirmStateChange = { false }
        )
    )

    val coroutineScope = rememberCoroutineScope()
    val pickerType = remember { mutableStateOf<@PickerType Int>(PickerType.NONE) }
    val focusManager = LocalFocusManager.current
    val toggleBottomSheet: (Int) -> Unit = { p ->
        focusManager.clearFocus(true)
        pickerType.value = p
        coroutineScope.launch {
            when {
                p == PickerType.NONE -> bottomSheetScaffoldState.bottomSheetState.collapse()
                bottomSheetScaffoldState.bottomSheetState.isCollapsed -> bottomSheetScaffoldState.bottomSheetState.expand()
            }
        }
    }

    BackHandler(enabled = bottomSheetScaffoldState.bottomSheetState.isExpanded) {
        toggleBottomSheet(PickerType.NONE)
    }

    val sheetContentHeight = remember { mutableStateOf(1) }
    val sheetPaddingBottom = derivedStateOf { sheetContentHeight.value }

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetBackgroundColor = MaterialTheme.colors.surface,
        sheetGesturesEnabled = false,
        sheetContent = {
            CreateProfileBottomSheetPicker(
                sheetContentHeight = sheetContentHeight,
                state = state,
                pickerType = pickerType.value,
                actionHandler = actionHandler
            )
        },
        sheetPeekHeight = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pickerBottomSheetWithPaddings(
                    enabled = pickerType.value != PickerType.NONE,
                    paddingBottom = pxToDp(px = sheetPaddingBottom.value)
                )
                .navigationBarsWithImePadding()
        ) {
            ScreenCreateProfile(
                boxScope = this,
                state = state,
                toggleBottomSheet = toggleBottomSheet,
                actionHandler = actionHandler
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .noRippleClickableConditioned(derivedStateOf { pickerType.value != PickerType.NONE }) {
                        if (pickerType.value != PickerType.NONE) {
                            toggleBottomSheet(PickerType.NONE)
                        }
                    }
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
private fun ScreenCreateProfile(
    boxScope: BoxScope,
    state: StateCreateProfile,
    toggleBottomSheet: (@PickerType Int) -> Unit,
    actionHandler: (ActionCreateProfile) -> Unit
) {
    CreateProfileForm(
        boxScope = boxScope,
        state = state,
        toggleBottomSheet = toggleBottomSheet,
        actionHandler = actionHandler
    )
}

@Preview("ScreenPreview", showSystemUi = true)
@Composable
private fun ScreenPreview() {
    AppTheme {
        ScreenCreateProfile(state = StateCreateProfile()) {

        }
    }
}