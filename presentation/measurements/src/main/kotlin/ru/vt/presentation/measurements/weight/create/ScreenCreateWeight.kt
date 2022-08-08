package ru.vt.presentation.measurements.weight.create

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.vt.core.resources.R
import ru.vt.core.ui.compose.collectWithLifecycle
import ru.vt.core.ui.compose.components.form.FormDivider
import ru.vt.core.ui.compose.components.form.FormEditableValue
import ru.vt.core.ui.compose.components.form.FormItemRow
import ru.vt.core.ui.compose.components.picker.DatePickerComposable
import ru.vt.core.ui.compose.components.picker.TimePickerComposable
import ru.vt.core.ui.compose.components.picker.WeightPickerComposable
import ru.vt.core.ui.compose.components.text.requiredAnnotatedString
import ru.vt.core.ui.compose.components.visibility.FadeInOutAnimatedVisibility
import ru.vt.core.ui.compose.theme.Normal
import ru.vt.core.ui.compose.utils.convertWeightToString
import ru.vt.core.ui.compose.utils.noRippleClickableConditioned
import ru.vt.core.ui.compose.utils.textResource
import ru.vt.core.ui.viewmodel.withFactory
import ru.vt.presentation.measurements.common.compose.CreateMeasurementHeader
import ru.vt.presentation.measurements.weight.create.ScreenCreateWeightViewModel.Action
import ru.vt.presentation.measurements.weight.create.ScreenCreateWeightViewModel.State as VMState

private enum class FormFocus {
    DATE, TIME, WEIGHT, UNDEFINED
}

@Composable
fun ScreenCreateWeight(profileId: Long) {
    val vm: ScreenCreateWeightViewModel =
        viewModel(factory = withFactory(factory = ScreenCreateWeightViewModel.Factory()))

    SideEffect {
        vm.submitAction(Action.Init(profileId = profileId))
    }

    Screen(vm = vm, initialState = vm.initialState())
}

@Composable
private fun Screen(
    vm: ScreenCreateWeightViewModel,
    initialState: VMState
) {
    val viewState by collectWithLifecycle(flow = vm.stateFlow, initial = initialState)

    Screen(state = viewState, actionHandler = {
        vm.submitAction(it)
    })
}

@Composable
private fun Screen(
    state: VMState,
    actionHandler: (Action) -> Unit
) {
    val focus = remember { mutableStateOf(FormFocus.UNDEFINED) }
    val toggleFormFocus: (FormFocus) -> Unit = {
        focus.value = it
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CreateMeasurementHeader(
                title = textResource(id = R.string.new_record),
                onBackPressed = {
                    actionHandler(Action.OnBackPressed)
                },
                onAddPressed = {
                    actionHandler(Action.Add)
                }
            )

            Body(
                state = state,
                currentFocus = derivedStateOf { focus.value }.value,
                toggleFormFocus = toggleFormFocus
            )

            Pickers(
                state = state,
                focus = derivedStateOf { focus.value }.value,
                toggleFormFocus = toggleFormFocus,
                actionHandler = actionHandler
            )
        }
    }
}

@Composable
private fun Body(
    state: VMState,
    currentFocus: FormFocus,
    toggleFormFocus: (FormFocus) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Normal)
            .background(
                color = MaterialTheme.colors.surface,
                shape = MaterialTheme.shapes.medium
            )
    ) {
        val focusManager = LocalFocusManager.current
        PickerItems(
            focusManager = focusManager,
            toggleFormFocus = toggleFormFocus,
            title = R.string.date,
            formFocus = FormFocus.DATE,
            editableText = state.getDateString(),
            inFocus = currentFocus == FormFocus.DATE
        )

        FormDivider()

        PickerItems(
            focusManager = focusManager,
            toggleFormFocus = toggleFormFocus,
            title = R.string.time,
            formFocus = FormFocus.TIME,
            editableText = state.getTimeString(),
            inFocus = currentFocus == FormFocus.TIME
        )

        FormDivider()

        PickerItems(
            focusManager = focusManager,
            toggleFormFocus = toggleFormFocus,
            title = R.string.weight,
            formFocus = FormFocus.WEIGHT,
            editableText = state.weightInGrams.convertWeightToString(),
            inFocus = currentFocus == FormFocus.WEIGHT
        )
    }
}

@Composable
private fun Pickers(
    state: VMState,
    focus: FormFocus,
    toggleFormFocus: (FormFocus) -> Unit,
    actionHandler: (Action) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .noRippleClickableConditioned(derivedStateOf { focus != FormFocus.UNDEFINED }) {
                if (focus != FormFocus.UNDEFINED) {
                    toggleFormFocus(FormFocus.UNDEFINED)
                }
            },
        contentAlignment = Alignment.BottomCenter
    ) {
        FadeInOutAnimatedVisibility(isVisible = focus == FormFocus.DATE) {
            DatePickerComposable(
                modifier = Modifier.background(color = MaterialTheme.colors.surface),
                localDate = state.date.toLocalDate(),
                onDateSelected = {
                    actionHandler(Action.SetDate(it))
                })
        }
        FadeInOutAnimatedVisibility(isVisible = focus == FormFocus.TIME) {
            TimePickerComposable(
                modifier = Modifier.background(color = MaterialTheme.colors.surface),
                localTime = state.date.toLocalTime(),
                onTimeChanged = {
                    actionHandler(Action.SetTime(it))
                }
            )
        }
        FadeInOutAnimatedVisibility(isVisible = focus == FormFocus.WEIGHT) {
            WeightPickerComposable(
                modifier = Modifier.background(color = MaterialTheme.colors.surface),
                weight = state.weightInGrams,
                onWeightChanged = { w ->
                    actionHandler(Action.SetWeight(w))
                }
            )
        }
    }
}


@Composable
private fun PickerItems(
    focusManager: FocusManager,
    @StringRes title: Int,
    formFocus: FormFocus,
    editableText: String,
    inFocus: Boolean,
    toggleFormFocus: (FormFocus) -> Unit
) {
    FormItemRow(
        focusManager = focusManager,
        annotatedTitle = requiredAnnotatedString(append = title),
        onClick = {
            focusManager.clearFocus()
            toggleFormFocus(formFocus)
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FormEditableValue(
                text = editableText,
                color = if (inFocus) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.primary
            )
        }
    }
}
