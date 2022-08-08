package ru.vt.domain.dashboard.entity

import ru.vt.domain.common.Measurement

data class TrackedEntity(
    @Measurement val type: Int,
    val isTracked: Boolean
)
