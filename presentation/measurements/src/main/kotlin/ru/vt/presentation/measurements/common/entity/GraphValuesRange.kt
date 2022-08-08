package ru.vt.presentation.measurements.common.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class GraphValuesRange(
    val max: Int = 0,
    val min: Int = 0,
    val steps: Int = 4,
    val rangeToTxt: (Int) -> String = { it.toString() }
) : Parcelable