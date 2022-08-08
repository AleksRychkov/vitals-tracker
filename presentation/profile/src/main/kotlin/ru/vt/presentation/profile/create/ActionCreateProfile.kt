package ru.vt.presentation.profile.create

import ru.vt.domain.common.AppGender

internal sealed class ActionCreateProfile {
    object CreateProfile : ActionCreateProfile()
    data class UserInput(
        val name: String? = null,
        val dateOfBirth: BirthDay? = null,
        val gender: AppGender = AppGender.UNDEFINED,
        val heightCm: Int? = null,
        val weightG: Int? = null
    ) : ActionCreateProfile()
}