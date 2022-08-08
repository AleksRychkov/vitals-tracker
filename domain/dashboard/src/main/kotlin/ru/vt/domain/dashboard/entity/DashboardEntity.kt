package ru.vt.domain.dashboard.entity

import ru.vt.domain.common.Measurement
import ru.vt.domain.common.indexOfFirst
import ru.vt.domain.common.indexOfLast

data class DashboardEntity(
    val profileId: Long,
    val trackedItems: List<TrackedEntity>
) {
    constructor(profileId: Long) : this(
        profileId = profileId,
        trackedItems = (Measurement.indexOfFirst()..Measurement.indexOfLast())
            .map { TrackedEntity(it, false) }
    )
}

