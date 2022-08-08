package ru.vt.data.datasource

import ru.vt.domain.profile.entity.ProfileEntity

internal interface ProfileDataSource {

    suspend fun getProfileById(id: Long): ProfileEntity?

    suspend fun getProfiles(): List<ProfileEntity>

    suspend fun getProfilesSize(): Int

    suspend fun saveProfile(profileEntity: ProfileEntity): Long

    suspend fun deleteProfile(id: Long)

    suspend fun isNameAvailable(name: String): Boolean
}