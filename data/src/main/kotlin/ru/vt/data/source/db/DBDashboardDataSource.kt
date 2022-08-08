package ru.vt.data.source.db

import androidx.annotation.VisibleForTesting
import ru.vt.data.datasource.DashboardDataSource
import ru.vt.database.VitalsTrackerDB
import ru.vt.database.entity.DashboardEntityDb
import ru.vt.domain.common.Measurement
import ru.vt.domain.common.indexOfFirst
import ru.vt.domain.common.indexOfLast
import ru.vt.domain.dashboard.entity.DashboardEntity
import ru.vt.domain.dashboard.entity.TrackedEntity

internal class DBDashboardDataSource(
    private val db: VitalsTrackerDB
) : DashboardDataSource {

    private companion object {
        const val DELIMITER = ","
    }

    override suspend fun getDashboard(profileId: Long): DashboardEntity? =
        db.dashboardDao().getDashboard(profileId)?.let { Mapper.mapFromDB(it) }

    override suspend fun createDashboard(profileId: Long): DashboardEntity {
        db.dashboardDao().saveDashboard(DashboardEntityDb(profileId, ""))
        return requireNotNull(getDashboard(profileId))
    }

    override suspend fun updateTrackedItems(
        profileId: Long,
        trackedItems: List<TrackedEntity>
    ): DashboardEntity {
        val newTrackedItems = trackedItems
            .filter { it.isTracked }
            .map { it.type }
            .joinToString(DELIMITER)
        db.dashboardDao().updateTrackedItems(profileId, newTrackedItems)
        return requireNotNull(getDashboard(profileId))
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    object Mapper {
        fun mapFromDB(db: DashboardEntityDb): DashboardEntity {
            val trackedItems = if (db.trackedItems.isNotEmpty())
                db.trackedItems.split(DELIMITER).map { it.toInt() }
            else emptyList()
            return DashboardEntity(
                profileId = db.profileId,
                trackedItems = (Measurement.indexOfFirst()..Measurement.indexOfLast()).map { type ->
                    TrackedEntity(type = type, isTracked = trackedItems.contains(type))
                }
            )
        }
    }
}
