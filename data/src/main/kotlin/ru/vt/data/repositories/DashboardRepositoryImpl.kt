package ru.vt.data.repositories

import ru.vt.data.datasource.DashboardDataSource
import ru.vt.domain.dashboard.entity.DashboardEntity
import ru.vt.domain.dashboard.entity.TrackedEntity
import ru.vt.domain.dashboard.repository.DashboardRepository

internal class DashboardRepositoryImpl(
    private val dashboardDataSource: DashboardDataSource
) : DashboardRepository {

    override suspend fun getDashboard(profileId: Long): DashboardEntity {
        var dashboard = dashboardDataSource.getDashboard(profileId)
        if (dashboard == null) {
            dashboard = dashboardDataSource.createDashboard(profileId)
        }
        return dashboard
    }

    override suspend fun updateTrackedItems(
        profileId: Long, trackedItems: List<TrackedEntity>
    ): DashboardEntity = dashboardDataSource.updateTrackedItems(profileId, trackedItems)
}
