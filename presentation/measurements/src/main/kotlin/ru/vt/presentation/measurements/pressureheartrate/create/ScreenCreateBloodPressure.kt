package ru.vt.presentation.measurements.pressureheartrate.create

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.vt.core.resources.R
import ru.vt.core.ui.compose.collectWithLifecycle
import ru.vt.core.ui.compose.components.text.TextFieldAlignEndComposable
import ru.vt.core.ui.compose.components.form.FormDivider
import ru.vt.core.ui.compose.components.form.FormEditableValue
import ru.vt.core.ui.compose.components.form.FormItemRow
import ru.vt.core.ui.compose.components.picker.DatePickerComposable
import ru.vt.core.ui.compose.components.picker.TimePickerComposable
import ru.vt.core.ui.compose.components.text.requiredAnnotatedString
import ru.vt.core.ui.compose.components.visibility.FadeInOutAnimatedVisibility
import ru.vt.core.ui.compose.utils.textResource
import ru.vt.core.ui.compose.theme.Normal
import ru.vt.core.ui.compose.theme.Normal2X
import ru.vt.core.ui.compose.utils.noRippleClickable
import ru.vt.core.ui.viewmodel.withFactory
import ru.vt.domain.common.MeasurementParams
import ru.vt.presentation.measurements.common.compose.CreateMeasurementHeader
import ru.vt.presentation.measurements.pressureheartrate.create.ScreenCreatePHRViewModel.Action
import ru.vt.presentation.measurements.pressureheartrate.create.ScreenCreatePHRViewModel.State

private enum class FormFocus {
    DATE, TIME, UNDEFINED
}

@Composable
fun ScreenCreateBloodPressure(profileId: Long) {
    val vm: ScreenCreatePHRViewModel =
        viewModel(factory = withFactory(factory = ScreenCreatePHRViewModel.Factory()))

    SideEffect(effect = {
        vm.submitAction(Action.Init(profileId = profileId))
    })

    Screen(vm = vm, initialState = vm.initialState())
}

@Composable
private fun Screen(
    vm: ScreenCreatePHRViewModel,
    initialState: State
) {
    val viewState by collectWithLifecycle(flow = vm.stateFlow, initial = initialState)

    Screen(state = derivedStateOf { viewState }.value) {
        vm.submitAction(it)
    }
}

@Composable
private fun Screen(
    state: State,
    actionHandler: (Action) -> Unit
) {
    val focus = remember { mutableStateOf(FormFocus.UNDEFINED) }
    val toggleFormFocus: (FormFocus) -> Unit = {
        focus.value = it
    }
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
            toggleFormFocus = toggleFormFocus,
            actionHandler = actionHandler
        )

        Box(
            modifier = Modifier
                .weight(weight = 1f, fill = true)
                .noRippleClickable {
                    if (focus.value == FormFocus.DATE || focus.value == FormFocus.TIME) {
                        toggleFormFocus(FormFocus.UNDEFINED)
                    }
                },
            contentAlignment = Alignment.BottomCenter
        ) {
            FadeInOutAnimatedVisibility(isVisible = focus.value == FormFocus.DATE) {
                DatePickerComposable(
                    modifier = Modifier.background(color = MaterialTheme.colors.surface),
                    localDate = state.date.toLocalDate(),
                    onDateSelected = {
                        actionHandler(Action.SetDate(it))
                    })
            }
            FadeInOutAnimatedVisibility(isVisible = focus.value == FormFocus.TIME) {
                TimePickerComposable(
                    modifier = Modifier.background(color = MaterialTheme.colors.surface),
                    localTime = state.date.toLocalTime(),
                    onTimeChanged = {
                        actionHandler(Action.SetTime(it))
                    }
                )
            }
        }
    }
}

@Composable
private fun Body(
    state: State,
    currentFocus: FormFocus,
    toggleFormFocus: (FormFocus) -> Unit,
    actionHandler: (Action) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
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

        ValueItem(
            focusManager = focusManager,
            title = requiredAnnotatedString(append = textResource(key = MeasurementParams.SYSTOLIC.key)),
            value = state.systolic?.toString(),
            toggleFormFocus = toggleFormFocus,
            onValueChanged = {
                actionHandler(Action.SetSystolic(it))
            },
            isLastItem = false
        )

        FormDivider()

        ValueItem(
            focusManager = focusManager,
            title = requiredAnnotatedString(append = textResource(key = MeasurementParams.DIASTOLIC.key)),
            value = state.diastolic?.toString(),
            toggleFormFocus = toggleFormFocus,
            onValueChanged = {
                actionHandler(Action.SetDiastolic(it))
            },
            isLastItem = false
        )

        FormDivider()

        ValueItem(
            focusManager = focusManager,
            title = buildAnnotatedString {
                append(textResource(key = MeasurementParams.HEART_RATE.key))
            },
            value = state.heartRate?.toString(),
            toggleFormFocus = toggleFormFocus,
            onValueChanged = {
                actionHandler(Action.SetHeartRate(it))
            },
            isLastItem = true
        )
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
        title = title,
        onClick = {
            focusManager.clearFocus()
            toggleFormFocus(formFocus)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
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

@Composable
private fun ValueItem(
    focusManager: FocusManager,
    title: AnnotatedString,
    value: String?,
    toggleFormFocus: (FormFocus) -> Unit,
    onValueChanged: (String) -> Unit,
    isLastItem: Boolean
) {
    val focusRequester = FocusRequester()
    FormItemRow(
        focusManager = focusManager,
        annotatedTitle = title,
        onClick = {
            toggleFormFocus(FormFocus.UNDEFINED)
            focusRequester.requestFocus()
        }
    ) {
        val keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = if (isLastItem) ImeAction.Done else ImeAction.Next
        )
        TextFieldAlignEndComposable(
            modifier = Modifier
                .weight(1f)
                .padding(start = Normal2X),
            focusManager = focusManager,
            focusRequester = focusRequester,
            value = value,
            onValueChange = onValueChanged,
            hint = null,
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus(true) },
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            onFocusChanged = {
                if (it) {
                    toggleFormFocus(FormFocus.UNDEFINED)
                }
            }
        )
    }
}
