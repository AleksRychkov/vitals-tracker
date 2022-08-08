package ru.vt.domain.profile.entity

import ru.vt.domain.common.AppGender
import ru.vt.domain.common.SimpleDateEntity

data class ProfileEntity(
    val id: Long,
    val name: String,
    val birth: SimpleDateEntity? = null,
    val gender: AppGender? = null,
    val heightCm: Int? = null,
    val weightG: Int? = null
)