package ru.vt.data.repositories

import ru.vt.data.datasource.ProfileDataSource
import ru.vt.data.providers.ProfileProvider
import ru.vt.domain.profile.entity.ProfileEntity
import ru.vt.domain.profile.repository.ProfileRepository

internal class ProfileRepositoryImpl(
    private val profileDataSource: ProfileDataSource,
    private val profileProvider: ProfileProvider
) : ProfileRepository {

    override suspend fun getProfiles(): List<ProfileEntity> =
        profileDataSource.getProfiles()

    override suspend fun saveProfile(profileEntity: ProfileEntity): Long {
        val id = profileDataSource.saveProfile(profileEntity)
        if (profileDataSource.getProfilesSize() == 1) {
            profileProvider.defaultProfileId = id
        }
        return id
    }

    override suspend fun deleteProfile(id: Long) {
        profileDataSource.deleteProfile(id)
        if (id == profileProvider.defaultProfileId) {
            profileProvider.defaultProfileId = null
        }
    }

    override suspend fun isNameAvailable(name: String): Boolean =
        profileDataSource.isNameAvailable(name)

    override suspend fun getDefaultProfile(): ProfileEntity? {
        val defaultProfileId = profileProvider.defaultProfileId
        var defaultProfile: ProfileEntity? = null
        if (defaultProfileId != null) {
            val profile = profileDataSource.getProfileById(defaultProfileId)
            if (profile == null) {
                profileProvider.defaultProfileId = null
            } else {
                defaultProfile = profile
            }
        }
        val profilesSize = profileDataSource.getProfilesSize()
        if (defaultProfile == null && profilesSize > 0) {
            defaultProfile = profileDataSource.getProfiles().first().also {
                profileProvider.defaultProfileId = it.id
            }
        }
        return defaultProfile
    }

    override suspend fun setDefaultProfile(id: Long) {
        profileProvider.defaultProfileId = id
    }

    override suspend fun getProfileById(id: Long): ProfileEntity =
        requireNotNull(profileDataSource.getProfileById(id))
}
