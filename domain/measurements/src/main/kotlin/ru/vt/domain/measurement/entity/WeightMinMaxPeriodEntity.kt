package ru.vt.domain.measurement.entity

data class WeightMinMaxPeriodEntity(
    val bound: Pair<Long, Long>,
    val weightMax: Int,
    val weightMin: Int
)