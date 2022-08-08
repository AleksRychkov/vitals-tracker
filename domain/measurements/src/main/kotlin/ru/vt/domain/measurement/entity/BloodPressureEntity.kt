package ru.vt.domain.measurement.entity

import ru.vt.domain.common.Measurement

data class BloodPressureEntity(
    override val profileId: Long,
    override val timestamp: Long,
    val systolic: Int,
    val diastolic: Int,
    val heartRate: Int? = null
) : MeasurementEntity(profileId, Measurement.PRESSURE_AND_HEART_RATE, timestamp) {
    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
