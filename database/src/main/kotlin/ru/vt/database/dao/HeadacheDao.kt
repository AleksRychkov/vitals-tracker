package ru.vt.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import ru.vt.database.Common
import ru.vt.database.entity.HeadacheEntityDb
import ru.vt.database.entity.raw.HeadacheMinMaxPeriodPojo

@Dao
interface HeadacheDao {

    @Query("SELECT * FROM ${Common.TABLE_HEADACHE} WHERE profile_id=:profileId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatest(profileId: Long): HeadacheEntityDb?

    @Insert
    suspend fun save(entityDb: HeadacheEntityDb): Long

    @Query("SELECT * FROM ${Common.TABLE_HEADACHE} WHERE profile_id=:profileId AND timestamp>=:from AND timestamp<:to ORDER BY timestamp ASC LIMIT :limit OFFSET :offset")
    suspend fun getForPeriod(
        profileId: Long,
        from: Long,
        to: Long,
        offset: Int,
        limit: Int
    ): List<HeadacheEntityDb>

    @Query("DELETE FROM ${Common.TABLE_HEADACHE} WHERE profile_id=:profileId AND timestamp=:timestamp")
    suspend fun delete(profileId: Long, timestamp: Long): Int

    @Query("DELETE FROM ${Common.TABLE_HEADACHE}")
    suspend fun clear()

    @RawQuery
    suspend fun getMinMaxPeriod(query: SupportSQLiteQuery): List<HeadacheMinMaxPeriodPojo>
}
