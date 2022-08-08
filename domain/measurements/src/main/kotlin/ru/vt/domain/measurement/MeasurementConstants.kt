package ru.vt.domain.measurement

object MeasurementConstants {
    const val MIN_SYSTOLIC = 50
    const val MAX_SYSTOLIC = 200

    const val MIN_DIASTOLIC = 50
    const val MAX_DIASTOLIC = 200

    const val MIN_HEART_RATE = 20
    const val MAX_HEART_RATE = 200

    val MIN_MAX_PHR: Pair<Int, Int> = 20 to 200

    const val HEADACHE_INTENSITY_MIN = 1
    const val HEADACHE_INTENSITY_MAX = 10
}
