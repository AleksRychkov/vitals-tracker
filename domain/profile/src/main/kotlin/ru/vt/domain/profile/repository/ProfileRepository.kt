package ru.vt.domain.profile.repository

import ru.vt.domain.profile.entity.ProfileEntity

interface ProfileRepository {

    suspend fun setDefaultProfile(id: Long)

    suspend fun getProfileById(id: Long): ProfileEntity

    suspend fun getDefaultProfile(): ProfileEntity?

    suspend fun getProfiles(): List<ProfileEntity>

    suspend fun saveProfile(profileEntity: ProfileEntity): Long

    suspend fun deleteProfile(id: Long)

    suspend fun isNameAvailable(name: String): Boolean
}
