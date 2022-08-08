package ru.vt.presentation.dashboard.main

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.vt.domain.common.Measurement
import ru.vt.domain.dashboard.entity.TrackedEntity

@Parcelize
internal data class StateDashboard(
    val profileName: String? = null,
    val profileId: Long = -1,
    val trackedItems: List<TrackedItem> = emptyList(),
    val hasTrackedItems: Boolean = false
) : Parcelable

@Parcelize
internal data class TrackedItem(
    @Measurement val type: Int,
    val isTracked: Boolean
) : Parcelable

internal fun mapTrackedItem(list: List<TrackedEntity>): List<TrackedItem> =
    list.map { TrackedItem(type = it.type, isTracked = it.isTracked) }
