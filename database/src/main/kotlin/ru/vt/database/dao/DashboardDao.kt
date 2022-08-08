package ru.vt.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.vt.database.Common
import ru.vt.database.entity.DashboardEntityDb

@Dao
interface DashboardDao {

    @Query("SELECT * FROM ${Common.TABLE_DASHBOARD} WHERE profile_id=:profileId")
    suspend fun getDashboard(profileId: Long): DashboardEntityDb?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDashboard(dashboardEntityDb: DashboardEntityDb): Long

    @Query("UPDATE ${Common.TABLE_DASHBOARD} SET tracked_items=:trackedItems WHERE profile_id=:profileId")
    suspend fun updateTrackedItems(profileId: Long, trackedItems: String)
}
