package ru.vt.domain.measurement.entity

data class BloodPressureMinMaxPeriodEntity(
    val bound: Pair<Long, Long>,
    val systolicMax: Int,
    val systolicMin: Int,
    val diastolicMax: Int,
    val diastolicMin: Int,
    val heartRateMax: Int? = null,
    val heartRateMin: Int? = null
)
