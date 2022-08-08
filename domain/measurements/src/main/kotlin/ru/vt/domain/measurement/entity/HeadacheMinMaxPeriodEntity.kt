package ru.vt.domain.measurement.entity

data class HeadacheMinMaxPeriodEntity(
    val bound: Pair<Long, Long>,
    val intensityMax: Int,
    val intensityMin: Int
)
