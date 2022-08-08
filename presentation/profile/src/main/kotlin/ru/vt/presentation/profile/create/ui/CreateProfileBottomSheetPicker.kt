package ru.vt.presentation.profile.create.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import com.google.accompanist.insets.navigationBarsPadding
import ru.vt.core.ui.compose.components.picker.DatePickerComposable
import ru.vt.core.ui.compose.components.picker.GenderPickerComposable
import ru.vt.core.ui.compose.components.picker.HeightPickerComposable
import ru.vt.core.ui.compose.components.picker.WeightPickerComposable
import ru.vt.core.ui.compose.components.visibility.FadeInOutAnimatedVisibility
import ru.vt.core.ui.compose.utils.pxToDp
import ru.vt.domain.common.AppGender
import ru.vt.presentation.profile.create.ActionCreateProfile
import ru.vt.presentation.profile.create.BirthDay
import ru.vt.presentation.profile.create.StateCreateProfile

@Composable
internal fun CreateProfileBottomSheetPicker(
    sheetContentHeight: MutableState<Int>,
    state: StateCreateProfile,
    pickerType: @PickerType Int,
    actionHandler: (ActionCreateProfile) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .navigationBarsPadding()
    ) {
        val modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned {
                if (it.size.height > sheetContentHeight.value) {
                    sheetContentHeight.value = it.size.height
                }
            }
        SetupPicker(
            modifier = modifier,
            state = state,
            actionHandler = actionHandler,
            sheetContentHeight = sheetContentHeight.value,
            pickerType = pickerType
        )
    }
}


@Composable
private inline fun SetupPicker(
    modifier: Modifier,
    state: StateCreateProfile,
    sheetContentHeight: Int,
    pickerType: @PickerType Int,
    crossinline actionHandler: (ActionCreateProfile) -> Unit,
) {
    FadeInOutAnimatedVisibility(isVisible = pickerType == PickerType.DATE) {
        DatePickerHandler(
            modifier = modifier,
            state = state,
            actionHandler = actionHandler
        )
    }
    FadeInOutAnimatedVisibility(isVisible = pickerType == PickerType.GENDER) {
        GenderPickerHandler(
            modifier = modifier,
            state = state,
            actionHandler = actionHandler
        )
    }
    FadeInOutAnimatedVisibility(isVisible = pickerType == PickerType.HEIGHT) {
        HeightPickerHandler(
            modifier = modifier,
            state = state,
            actionHandler = actionHandler
        )
    }
    FadeInOutAnimatedVisibility(isVisible = pickerType == PickerType.WEIGHT) {
        WeightPickerHandler(
            modifier = modifier,
            state = state,
            actionHandler = actionHandler
        )
    }
    AnimatedVisibility(visible = pickerType == PickerType.NONE) {
        Box(
            modifier = modifier
                .height(pxToDp(px = sheetContentHeight))
                .background(color = MaterialTheme.colors.surface)
        )
    }
}


@Composable
private inline fun GenderPickerHandler(
    modifier: Modifier,
    state: StateCreateProfile,
    crossinline actionHandler: (ActionCreateProfile) -> Unit
) {
    val gender = state.gender
    GenderPickerComposable(
        modifier = modifier,
        current = gender.key,
        genders = AppGender.values().map { it.key },
        onGenderSelected = { newGender ->
            actionHandler(
                ActionCreateProfile.UserInput(
                    name = state.name,
                    dateOfBirth = state.dateOfBirth,
                    gender = AppGender.valueFromKey(newGender),
                    heightCm = state.heightCm,
                    weightG = state.weightG
                )
            )
        }
    )
}

@Composable
private inline fun DatePickerHandler(
    modifier: Modifier,
    state: StateCreateProfile,
    crossinline actionHandler: (ActionCreateProfile) -> Unit
) {
    val date: Triple<Int, Int, Int>? = state.dateOfBirth?.let { Triple(it.day, it.month, it.year) }
    DatePickerComposable(modifier = modifier, date = date, onDateSelected = { t ->
        actionHandler(
            ActionCreateProfile.UserInput(
                name = state.name,
                dateOfBirth = BirthDay(t.first, t.second, t.third),
                gender = state.gender,
                heightCm = state.heightCm,
                weightG = state.weightG
            )
        )
    })
}

@Composable
private inline fun HeightPickerHandler(
    modifier: Modifier,
    state: StateCreateProfile,
    crossinline actionHandler: (ActionCreateProfile) -> Unit
) {
    HeightPickerComposable(
        modifier = modifier,
        height = state.heightCm,
        onHeightSelected = { h ->
            actionHandler(
                ActionCreateProfile.UserInput(
                    name = state.name,
                    dateOfBirth = state.dateOfBirth,
                    gender = state.gender,
                    heightCm = h,
                    weightG = state.weightG
                )
            )
        }
    )
}

@Composable
private inline fun WeightPickerHandler(
    modifier: Modifier,
    state: StateCreateProfile,
    crossinline actionHandler: (ActionCreateProfile) -> Unit
) {
    WeightPickerComposable(
        modifier = modifier,
        weight = state.weightG,
        onWeightChanged = { w ->
            actionHandler(
                ActionCreateProfile.UserInput(
                    name = state.name,
                    dateOfBirth = state.dateOfBirth,
                    gender = state.gender,
                    heightCm = state.heightCm,
                    weightG = w
                )
            )
        }
    )
}
