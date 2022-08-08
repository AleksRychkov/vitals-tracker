package ru.vt.presentation.profile.create.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.vt.core.resources.R
import ru.vt.core.ui.compose.components.AppButtonComposable
import ru.vt.core.ui.compose.components.form.FormDivider
import ru.vt.core.ui.compose.components.form.FormEditableValue
import ru.vt.core.ui.compose.components.form.FormItemRow
import ru.vt.core.ui.compose.components.text.TextFieldAlignEndComposable
import ru.vt.core.ui.compose.components.text.requiredAnnotatedString
import ru.vt.core.ui.compose.theme.Normal
import ru.vt.core.ui.compose.theme.Normal2X
import ru.vt.core.ui.compose.utils.textResource
import ru.vt.domain.common.AppGender
import ru.vt.presentation.profile.create.ActionCreateProfile
import ru.vt.presentation.profile.create.StateCreateProfile

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
internal fun CreateProfileForm(
    boxScope: BoxScope,
    state: StateCreateProfile,
    toggleBottomSheet: (@PickerType Int) -> Unit,
    actionHandler: (ActionCreateProfile) -> Unit
) {
    with(boxScope) {
        Column(
            modifier = Modifier
                .padding(Normal)
                .background(
                    color = MaterialTheme.colors.surface,
                    shape = MaterialTheme.shapes.medium
                )
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .align(alignment = Alignment.Center)

        ) {
            val focusManager = LocalFocusManager.current

            FormItemName(
                focusManager = focusManager,
                state = state,
                toggleBottomSheet = toggleBottomSheet,
                actionHandler = actionHandler
            )

            FormDivider()

            FormItemBirthDay(
                focusManager = focusManager,
                state = state,
                toggleBottomSheet = toggleBottomSheet,
                actionHandler = actionHandler
            )

            FormDivider()

            FormItemGender(
                focusManager = focusManager,
                state = state,
                toggleBottomSheet = toggleBottomSheet,
                actionHandler = actionHandler
            )

            FormDivider()

            FormItemHeight(
                focusManager = focusManager,
                state = state,
                toggleBottomSheet = toggleBottomSheet,
                actionHandler = actionHandler
            )

            FormDivider()

            FormItemWeight(
                focusManager = focusManager,
                state = state,
                toggleBottomSheet = toggleBottomSheet,
                actionHandler = actionHandler
            )

            FormDivider()

            AppButtonComposable(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                enabled = state.name.isNullOrEmpty().not(),
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = Normal,
                    bottomEnd = Normal
                ),
                onClick = {
                    focusManager.clearFocus(true)
                    toggleBottomSheet(PickerType.NONE)
                    actionHandler(ActionCreateProfile.CreateProfile)
                }
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = textResource(id = if (state.id == null) R.string.create else R.string.save).uppercase(),
                    style = MaterialTheme.typography.subtitle1.copy(
                        color = MaterialTheme.colors.secondary.copy(
                            alpha = if (state.name.isNullOrEmpty().not()) 1f else 0.5f
                        ),
                        letterSpacing = 2.sp,
                    )
                )
            }
        }
    }
}


@Composable
private fun FormItemName(
    focusManager: FocusManager,
    state: StateCreateProfile,
    toggleBottomSheet: (@PickerType Int) -> Unit,
    actionHandler: (ActionCreateProfile) -> Unit
) {
    val focusRequester = FocusRequester()
    FormItemRow(
        focusManager = focusManager,
        annotatedTitle = requiredAnnotatedString(append = R.string.name),
        onClick = {
            toggleBottomSheet(PickerType.NONE)
            focusRequester.requestFocus()
        }
    ) {
        val keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        )
        TextFieldAlignEndComposable(
            modifier = Modifier
                .weight(1f)
                .padding(start = Normal2X),
            focusManager = focusManager,
            focusRequester = focusRequester,
            value = state.name,
            onValueChange = {
                actionHandler(
                    ActionCreateProfile.UserInput(
                        name = it,
                        dateOfBirth = state.dateOfBirth,
                        gender = state.gender,
                        weightG = state.weightG,
                        heightCm = state.heightCm
                    )
                )
            },
            hint = textResource(id = R.string.create_profile_enter_name_hint),
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus(true) },
                onNext = { focusManager.clearFocus(true) }
            )
        )
    }
}

@Composable
private fun FormItemBirthDay(
    focusManager: FocusManager,
    state: StateCreateProfile,
    toggleBottomSheet: (@PickerType Int) -> Unit,
    actionHandler: (ActionCreateProfile) -> Unit
) {
    BasePickerItem(
        focusManager = focusManager,
        title = R.string.birthdate,
        hint = R.string.create_profile_enter_birthdate_hint,
        isFieldEmpty = state.dateOfBirth == null,
        cleanField = {
            ActionCreateProfile.UserInput(
                name = state.name,
                dateOfBirth = null,
                gender = state.gender,
                heightCm = state.heightCm,
                weightG = state.weightG
            )
        },
        fieldValue = state.getDateOfBirth(),
        pickerType = PickerType.DATE,
        toggleBottomSheet = toggleBottomSheet,
        actionHandler = actionHandler
    )
}

@Composable
private fun FormItemGender(
    focusManager: FocusManager,
    state: StateCreateProfile,
    toggleBottomSheet: (@PickerType Int) -> Unit,
    actionHandler: (ActionCreateProfile) -> Unit
) {
    BasePickerItem(
        focusManager = focusManager,
        title = R.string.gender,
        hint = R.string.create_profile_enter_gender_hint,
        isFieldEmpty = state.gender == AppGender.UNDEFINED,
        cleanField = {
            ActionCreateProfile.UserInput(
                name = state.name,
                dateOfBirth = state.dateOfBirth,
                gender = AppGender.UNDEFINED,
                heightCm = state.heightCm,
                weightG = state.weightG
            )
        },
        fieldValue = state.gender.key,
        pickerType = PickerType.GENDER,
        toggleBottomSheet = toggleBottomSheet,
        actionHandler = actionHandler
    )
}

@Composable
private fun FormItemHeight(
    focusManager: FocusManager,
    state: StateCreateProfile,
    toggleBottomSheet: (@PickerType Int) -> Unit,
    actionHandler: (ActionCreateProfile) -> Unit
) {
    BasePickerItem(
        focusManager = focusManager,
        title = R.string.height,
        hint = R.string.create_profile_enter_height_hint,
        isFieldEmpty = state.heightCm == null,
        cleanField = {
            ActionCreateProfile.UserInput(
                name = state.name,
                dateOfBirth = state.dateOfBirth,
                gender = state.gender,
                heightCm = null,
                weightG = state.weightG
            )
        },
        fieldValue = "${state.heightCm}cm",
        pickerType = PickerType.HEIGHT,
        toggleBottomSheet = toggleBottomSheet,
        actionHandler = actionHandler
    )
}

@Composable
private fun FormItemWeight(
    focusManager: FocusManager,
    state: StateCreateProfile,
    toggleBottomSheet: (@PickerType Int) -> Unit,
    actionHandler: (ActionCreateProfile) -> Unit
) {
    BasePickerItem(
        focusManager = focusManager,
        title = R.string.weight,
        hint = R.string.create_profile_enter_weight_hint,
        isFieldEmpty = state.weightG == null,
        cleanField = {
            ActionCreateProfile.UserInput(
                name = state.name,
                dateOfBirth = state.dateOfBirth,
                gender = state.gender,
                heightCm = state.heightCm,
                weightG = null
            )
        },
        fieldValue = state.getWeight("kg", "g"),
        pickerType = PickerType.WEIGHT,
        toggleBottomSheet = toggleBottomSheet,
        actionHandler = actionHandler
    )
}

@Composable
private inline fun BasePickerItem(
    focusManager: FocusManager,
    @StringRes title: Int,
    @StringRes hint: Int,
    isFieldEmpty: Boolean,
    fieldValue: String,
    pickerType: @PickerType Int,
    crossinline cleanField: () -> ActionCreateProfile,
    crossinline toggleBottomSheet: (@PickerType Int) -> Unit,
    crossinline actionHandler: (ActionCreateProfile) -> Unit
) {
    FormItemRow(
        focusManager = focusManager,
        title = title,
        onClick = { toggleBottomSheet(pickerType) }
    ) {
        if (isFieldEmpty) {
            Hint(text = hint)
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ValueCleaner {
                    toggleBottomSheet(PickerType.NONE)
                    actionHandler(cleanField())
                }
                FormEditableValue(text = fieldValue)
            }
        }
    }
}

@Composable
private fun Hint(@StringRes text: Int) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.CenterEnd
    ) {
        Text(
            text = textResource(id = text),
            color = MaterialTheme.colors.secondaryVariant,
            style = MaterialTheme.typography.subtitle2
        )
    }
}

@Composable
private inline fun ValueCleaner(crossinline cleanValue: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .clipToBounds()
            .padding(end = Normal)
            .clickable {
                cleanValue()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(16.dp, 16.dp),
            painter = rememberVectorPainter(Icons.Filled.Clear),
            tint = MaterialTheme.colors.error,
            contentDescription = "value_cleaner"
        )
    }
}


