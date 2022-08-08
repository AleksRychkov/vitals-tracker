package ru.vt.domain.common

import androidx.annotation.IntDef

@IntDef(Measurement.HEADACHE, Measurement.WEIGHT, Measurement.PRESSURE_AND_HEART_RATE)
@Retention(AnnotationRetention.SOURCE)
annotation class Measurement {
    companion object {
        const val PRESSURE_AND_HEART_RATE: Int = 0
        const val HEADACHE: Int = 1
        const val WEIGHT: Int = 2
    }
}

fun Measurement.Companion.indexOfFirst(): Int = PRESSURE_AND_HEART_RATE
fun Measurement.Companion.indexOfLast(): Int = WEIGHT
