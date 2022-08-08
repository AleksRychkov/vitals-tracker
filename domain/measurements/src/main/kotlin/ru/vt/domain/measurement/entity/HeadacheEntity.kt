package ru.vt.domain.measurement.entity

import ru.vt.domain.common.Measurement
import ru.vt.domain.common.MeasurementParams

data class HeadacheEntity(
    override val profileId: Long,
    override val timestamp: Long,
    val intensity: Int,
    val headacheArea: HeadacheArea = HeadacheArea.UNDEFINED,
    val description: String?
) : MeasurementEntity(profileId, Measurement.HEADACHE, timestamp) {
    companion object {
        private const val serialVersionUID: Long = 1L
    }
}

enum class HeadacheArea(val key: String) {
    UNDEFINED(""),
    ALL(MeasurementParams.HEADACHE_ALL.key),
    LEFT(MeasurementParams.HEADACHE_LEFT.key),
    RIGHT(MeasurementParams.HEADACHE_RIGHT.key),
    FOREHEAD(MeasurementParams.HEADACHE_FOREHEAD.key),
    BACK_HEAD(MeasurementParams.HEADACHE_BACK_HEAD.key);

    companion object {
        fun valueFromKey(key: String?): HeadacheArea =
            values().firstOrNull { it.key.lowercase() == key?.lowercase() } ?: UNDEFINED
    }
}
