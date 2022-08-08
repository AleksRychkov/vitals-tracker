package ru.vt.domain.measurement.entity

import ru.vt.domain.common.Measurement

data class WeightEntity(
    override val profileId: Long,
    override val timestamp: Long,
    val weightInGrams: Int
) : MeasurementEntity(profileId, Measurement.WEIGHT, timestamp) {
    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
