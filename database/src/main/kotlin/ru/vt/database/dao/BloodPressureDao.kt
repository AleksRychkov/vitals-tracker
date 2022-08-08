package ru.vt.database.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import ru.vt.database.Common
import ru.vt.database.entity.BloodPressureEntityDb
import ru.vt.database.entity.raw.BloodPressureMinMaxPeriodPojo

@Dao
interface BloodPressureDao {

    @Query("SELECT * FROM ${Common.TABLE_BLOOD_PRESSURE} WHERE profile_id=:profileId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatest(profileId: Long): BloodPressureEntityDb?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(entityDb: BloodPressureEntityDb): Long

    @Query("SELECT * FROM ${Common.TABLE_BLOOD_PRESSURE} WHERE profile_id=:profileId AND timestamp>=:from AND timestamp<:to ORDER BY timestamp ASC LIMIT :limit OFFSET :offset")
    suspend fun getForPeriod(
        profileId: Long,
        from: Long,
        to: Long,
        offset: Int,
        limit: Int
    ): List<BloodPressureEntityDb>

    @Query("DELETE FROM ${Common.TABLE_BLOOD_PRESSURE} WHERE profile_id=:profileId AND timestamp=:timestamp")
    suspend fun delete(profileId: Long, timestamp: Long): Int

    @Query("DELETE FROM ${Common.TABLE_BLOOD_PRESSURE}")
    suspend fun clear()

    @RawQuery
    suspend fun getMinMaxPeriod(query: SupportSQLiteQuery): List<BloodPressureMinMaxPeriodPojo>
}
