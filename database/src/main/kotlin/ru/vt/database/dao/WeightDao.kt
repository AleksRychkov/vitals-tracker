package ru.vt.database.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import ru.vt.database.Common
import ru.vt.database.entity.WeightEntityDb
import ru.vt.database.entity.raw.WeightMinMaxPeriodPojo

@Dao
interface WeightDao {

    @Query("SELECT * FROM ${Common.TABLE_WEIGHT} WHERE profile_id=:profileId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatest(profileId: Long): WeightEntityDb?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(entityDb: WeightEntityDb): Long

    @Query("SELECT * FROM ${Common.TABLE_WEIGHT} WHERE profile_id=:profileId AND timestamp>=:from AND timestamp<:to ORDER BY timestamp ASC LIMIT :limit OFFSET :offset")
    suspend fun getForPeriod(
        profileId: Long,
        from: Long,
        to: Long,
        offset: Int,
        limit: Int
    ): List<WeightEntityDb>

    @Query("DELETE FROM ${Common.TABLE_WEIGHT} WHERE profile_id=:profileId AND timestamp=:timestamp")
    suspend fun delete(profileId: Long, timestamp: Long): Int

    @Query("DELETE FROM ${Common.TABLE_WEIGHT}")
    suspend fun clear()

    @RawQuery
    suspend fun getMinMaxPeriod(query: SupportSQLiteQuery): List<WeightMinMaxPeriodPojo>
}
