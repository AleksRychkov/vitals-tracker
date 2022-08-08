package ru.vt.domain.dashboard.repository

import ru.vt.domain.dashboard.entity.DashboardEntity
import ru.vt.domain.dashboard.entity.TrackedEntity

interface DashboardRepository {

    suspend fun getDashboard(profileId: Long): DashboardEntity

    suspend fun updateTrackedItems(profileId: Long, trackedItems: List<TrackedEntity>): DashboardEntity
}
