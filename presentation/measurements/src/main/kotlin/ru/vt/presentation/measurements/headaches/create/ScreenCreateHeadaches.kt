package ru.vt.presentation.measurements.headaches.create

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.navigationBarsWithImePadding
import org.threeten.bp.LocalDateTime
import ru.vt.core.resources.R
import ru.vt.core.ui.compose.collectWithLifecycle
import ru.vt.core.ui.compose.components.form.FormDivider
import ru.vt.core.ui.compose.components.form.FormEditableValue
import ru.vt.core.ui.compose.components.form.FormItemRow
import ru.vt.core.ui.compose.components.picker.DatePickerComposable
import ru.vt.core.ui.compose.components.picker.GeneralPickerComposable
import ru.vt.core.ui.compose.components.picker.TimePickerComposable
import ru.vt.core.ui.compose.components.text.IgnoreTextToolbarWrapper
import ru.vt.core.ui.compose.components.text.requiredAnnotatedString
import ru.vt.core.ui.compose.components.visibility.FadeInOutAnimatedVisibility
import ru.vt.core.ui.compose.theme.*
import ru.vt.core.ui.compose.utils.noRippleClickable
import ru.vt.core.ui.compose.utils.noRippleClickableConditioned
import ru.vt.core.ui.compose.utils.textResource
import ru.vt.core.ui.viewmodel.withFactory
import ru.vt.domain.measurement.MeasurementConstants
import ru.vt.domain.measurement.entity.HeadacheArea
import ru.vt.presentation.measurements.common.compose.CreateMeasurementHeader
import ru.vt.presentation.measurements.headaches.create.ScreenCreateHeadachesViewModel.Action
import ru.vt.presentation.measurements.headaches.create.ScreenCreateHeadachesViewModel.State

private enum class FormFocus {
    DATE, TIME, HEADACHE_AREA, UNDEFINED
}

@Composable
fun ScreenCreateHeadaches(profileId: Long) {
    val vm: ScreenCreateHeadachesViewModel =
        viewModel(factory = withFactory(factory = ScreenCreateHeadachesViewModel.Factory()))

    SideEffect {
        vm.submitAction(Action.Init(profileId = profileId))
    }

    Screen(vm = vm, initialState = vm.initialState())
}

@Composable
private fun Screen(
    vm: ScreenCreateHeadachesViewModel,
    initialState: State
) {
    val viewState by collectWithLifecycle(flow = vm.stateFlow, initial = initialState)

    Screen(state = viewState, actionHandler = {
        vm.submitAction(it)
    })
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
                toggleFormFocus = toggleFormFocus,
                actionHandler = actionHandler
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .noRippleClickableConditioned(derivedStateOf { focus.value != FormFocus.UNDEFINED }) {
                    if (focus.value == FormFocus.DATE || focus.value == FormFocus.TIME || focus.value == FormFocus.HEADACHE_AREA) {
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
            FadeInOutAnimatedVisibility(isVisible = focus.value == FormFocus.HEADACHE_AREA) {
                val labels = mutableMapOf<HeadacheArea, String>()
                HeadacheArea.values().forEach {
                    labels[it] = textResource(key = it.key)
                }
                GeneralPickerComposable(
                    modifier = Modifier.background(color = MaterialTheme.colors.surface),
                    selectedValue = state.headacheArea,
                    data = HeadacheArea.values().toList(),
                    labels = labels
                ) {
                    actionHandler(Action.SetHeadacheArea(it))
                }
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
            .wrapContentHeight()
            .navigationBarsWithImePadding()
            .verticalScroll(rememberScrollState())
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

        HeadacheIntensity(actionHandler = actionHandler, selectedValue = state.headacheIntensity)

        FormDivider()

        HeadacheArea(
            selectedValue = state.headacheArea,
            focusManager = focusManager,
            toggleFormFocus = toggleFormFocus,
            inFocus = currentFocus == FormFocus.HEADACHE_AREA
        )

        FormDivider()

        AdditionalInfo(actionHandler = actionHandler, editableText = state.description)
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

@Composable
private fun HeadacheIntensity(
    actionHandler: (Action) -> Unit,
    selectedValue: Int? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(Normal),
    ) {

        Text(
            modifier = Modifier,
            text = requiredAnnotatedString(append = R.string.screen_create_headaches_intensity_desc),
            color = MaterialTheme.colors.secondaryVariant,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.subtitle1
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(top = Medium)
        ) {
            for (i in (MeasurementConstants.HEADACHE_INTENSITY_MIN..(MeasurementConstants.HEADACHE_INTENSITY_MAX / 2))) {
                ChipRow(
                    value = i,
                    selectedValue = selectedValue,
                    text = i.toString(),
                    rowScope = this
                ) { intensity ->
                    actionHandler(Action.SetHeadacheIntensity(intensity))
                }
                if (i != MeasurementConstants.HEADACHE_INTENSITY_MAX / 2) {
                    Spacer(modifier = Modifier.width(Tinny))
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(top = Tinny),
        ) {
            for (i in ((MeasurementConstants.HEADACHE_INTENSITY_MAX / 2 + 1)..MeasurementConstants.HEADACHE_INTENSITY_MAX)) {
                ChipRow(
                    value = i,
                    selectedValue = selectedValue,
                    text = i.toString(),
                    rowScope = this
                ) { intensity ->
                    actionHandler(Action.SetHeadacheIntensity(intensity))
                }
                if (i != MeasurementConstants.HEADACHE_INTENSITY_MAX) {
                    Spacer(modifier = Modifier.width(Tinny))
                }
            }
        }
    }
}

@Composable
private fun HeadacheArea(
    selectedValue: HeadacheArea,
    focusManager: FocusManager,
    inFocus: Boolean,
    toggleFormFocus: (FormFocus) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(Normal)
        .noRippleClickable {
            focusManager.clearFocus(true)
            toggleFormFocus(FormFocus.HEADACHE_AREA)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier,
                text = textResource(id = R.string.screen_create_headaches_area_desc),
                color = MaterialTheme.colors.secondaryVariant,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle1
            )
        }
        Box(
            modifier = Modifier
                .padding(top = Medium)
                .fillMaxWidth()
                .height(36.dp)
                .background(
                    color = MaterialTheme.colors.chips,
                    shape = RoundedCornerShape(10.dp)
                )
                .clip(RoundedCornerShape(10.dp))
                .clipToBounds(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(end = Normal),
                text = textResource(key = selectedValue.key),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle1.copy(color = if (inFocus) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.primary),
            )
        }
    }
}

@Composable
private fun AdditionalInfo(
    actionHandler: (Action) -> Unit,
    editableText: String?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Normal)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier,
                text = textResource(id = R.string.screen_create_headaches_additional_desc),
                color = MaterialTheme.colors.secondaryVariant,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle1
            )
        }

        Box(
            modifier = Modifier
                .padding(top = Medium)
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .background(
                    color = MaterialTheme.colors.chips,
                    shape = RoundedCornerShape(10.dp)
                )
                .clip(RoundedCornerShape(10.dp))
                .clipToBounds(),
            contentAlignment = Alignment.CenterStart
        ) {
            IgnoreTextToolbarWrapper {
                val isFocused = remember { mutableStateOf(false) }
                val focusManager = LocalFocusManager.current
                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Normal, vertical = Medium)
                        .onFocusChanged {
                            isFocused.value = it.isFocused
                        },
                    textStyle = MaterialTheme.typography.subtitle1.copy(
                        color = if (isFocused.value) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.primary
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colors.primaryVariant),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus(true) }
                    ),
                    value = editableText ?: "",
                    onValueChange = { desc ->
                        actionHandler(Action.SetHeadacheDescription(desc))
                    }
                )
            }
        }
    }
}

@Composable
private fun <Value> ChipRow(
    rowScope: RowScope,
    selectedValue: Value? = null,
    value: Value,
    text: String,
    onClick: (Value) -> Unit
) {
    with(rowScope) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(alignment = Alignment.CenterVertically)
                .weight(weight = 1f, fill = true)
                .height(36.dp)
                .background(
                    color = if (selectedValue == value) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.chips,
                    shape = RoundedCornerShape(10.dp)
                )
                .clip(RoundedCornerShape(10.dp))
                .clipToBounds()
                .clickable {
                    onClick(value)
                })
        {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body1.copy(color = if (selectedValue == value) Color.White else MaterialTheme.colors.primary)
            )
        }
    }
}

@Preview("ScreenCreateHeadaches preview", showSystemUi = true, showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        var state = remember {
            State(date = LocalDateTime.now())
        }
        Screen(state = state, actionHandler = {
            when (it) {
                is Action.Init -> state = state.copy(profileId = it.profileId)
                is Action.SetHeadacheIntensity -> state = state.copy(headacheIntensity = it.value)
                else -> {}
            }
        })
    }
}
