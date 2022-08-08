package ru.vt.presentation.measurements.common.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.threeten.bp.LocalDate

@Parcelize
internal data class AnchorDate(
    val day: Int,// 1..31
    val month: Int, // 1..12
    val year: Int,
    val daysInMonth: Int
) : Parcelable

internal fun LocalDate.toAnchorDate(): AnchorDate = AnchorDate(
    day = this.dayOfMonth,
    month = this.month.value,
    year = this.year,
    daysInMonth = this.month.length(this.isLeapYear)
)