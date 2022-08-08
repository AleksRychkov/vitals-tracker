package ru.vt.data.datasource

import ru.vt.domain.dashboard.entity.DashboardEntity
import ru.vt.domain.dashboard.entity.TrackedEntity

internal interface DashboardDataSource {
    suspend fun getDashboard(profileId: Long): DashboardEntity?
    suspend fun createDashboard(profileId: Long): DashboardEntity
    suspend fun updateTrackedItems(profileId: Long, trackedItems: List<TrackedEntity>): DashboardEntity
}
