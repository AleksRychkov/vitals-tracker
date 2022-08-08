package ru.vt.presentation.profile.create

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.vt.domain.common.AppGender
import ru.vt.domain.common.SimpleDateEntity

@Parcelize
internal data class StateCreateProfile(
    val id: Long? = null,
    val name: String? = null,
    val dateOfBirth: BirthDay? = null,
    val gender: AppGender = AppGender.UNDEFINED,
    val heightCm: Int? = null,
    val weightG: Int? = null
) : Parcelable {

    fun getDateOfBirth(): String = dateOfBirth?.let {
        "${it.day}.${String.format("%02d", it.month)}.${it.year}"
    } ?: ""

    fun getWeight(
        kgText: String,
        gText: String
    ): String = weightG?.let {
        val kg = it / 1000
        val g = it % 1000
        if (g > 0) {
            "${kg}$kgText ${g}$gText"
        } else {
            "${kg}$kgText"
        }
    } ?: ""
}

@Parcelize
internal data class BirthDay(
    val day: Int,// 1..31
    val month: Int, // 1..12
    val year: Int
) : Parcelable

internal fun BirthDay.toEntity(): SimpleDateEntity = SimpleDateEntity(day, month, year)
