package ru.vt.domain.measurement.entity

import ru.vt.domain.common.Measurement
import java.io.Serializable

abstract class MeasurementEntity(
    open val profileId: Long,
    @Measurement val type: Int,
    open val timestamp: Long
) : Serializable
