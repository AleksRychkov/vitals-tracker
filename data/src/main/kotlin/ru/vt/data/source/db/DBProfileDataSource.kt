package ru.vt.data.source.db

import androidx.annotation.VisibleForTesting
import ru.vt.data.datasource.ProfileDataSource
import ru.vt.database.VitalsTrackerDB
import ru.vt.database.entity.ProfileEntityDB
import ru.vt.domain.common.AppGender
import ru.vt.domain.common.SimpleDateEntity
import ru.vt.domain.profile.entity.ProfileEntity

internal class DBProfileDataSource(
    private val db: VitalsTrackerDB
) : ProfileDataSource {

    override suspend fun getProfileById(id: Long): ProfileEntity? =
        db.profileDao().getProfileById(id)?.let(Mapper::mapFromDb)

    override suspend fun getProfiles(): List<ProfileEntity> =
        db.profileDao().getAllProfiles().map(Mapper::mapFromDb)

    override suspend fun getProfilesSize(): Int = db.profileDao().getProfilesSize()

    override suspend fun saveProfile(profileEntity: ProfileEntity): Long =
        db.profileDao().createNewProfile(Mapper.mapToDb(profileEntity.copy(id = 0)))

    override suspend fun deleteProfile(id: Long) {
        db.profileDao().deleteProfile(id)
    }

    override suspend fun isNameAvailable(name: String): Boolean =
        db.profileDao().findByName(name).isEmpty()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    object Mapper {
        fun mapFromDb(db: ProfileEntityDB): ProfileEntity =
            ProfileEntity(
                id = db.profileId,
                name = db.name,
                birth = if (db.birthDay != null) SimpleDateEntity(
                    day = db.birthDay!!,
                    month = db.birthMonth!!,
                    year = db.birthYear!!
                ) else null,
                gender = if (db.gender != null) AppGender.valueFromKey(db.gender!!) else null,
                heightCm = db.heightCm,
                weightG = db.weightG
            )

        fun mapToDb(entity: ProfileEntity): ProfileEntityDB =
            ProfileEntityDB(
                profileId = entity.id,
                name = entity.name,
                birthDay = entity.birth?.day,
                birthMonth = entity.birth?.month,
                birthYear = entity.birth?.year,
                gender = entity.gender?.key,
                heightCm = entity.heightCm,
                weightG = entity.weightG
            )
    }
}
