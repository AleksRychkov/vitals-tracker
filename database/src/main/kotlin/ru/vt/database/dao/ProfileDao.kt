package ru.vt.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.vt.database.Common
import ru.vt.database.entity.ProfileEntityDB

@Dao
interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun createNewProfile(entity: ProfileEntityDB): Long

    @Query("SELECT * FROM ${Common.TABLE_PROFILE} WHERE profile_id = :id")
    suspend fun getProfileById(id: Long): ProfileEntityDB?

    @Query("SELECT * FROM ${Common.TABLE_PROFILE}")
    fun getAllProfiles(): List<ProfileEntityDB>

    @Query("DELETE FROM ${Common.TABLE_PROFILE} WHERE profile_id =:id")
    suspend fun deleteProfile(id: Long)

    @Query("SELECT * FROM ${Common.TABLE_PROFILE} WHERE name=:name")
    suspend fun findByName(name: String): List<ProfileEntityDB>

    @Query("SELECT COUNT(profile_id) FROM ${Common.TABLE_PROFILE}")
    suspend fun getProfilesSize(): Int
}
