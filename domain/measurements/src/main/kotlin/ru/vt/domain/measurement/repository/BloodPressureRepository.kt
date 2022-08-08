package ru.vt.domain.measurement.repository

import ru.vt.domain.measurement.entity.BloodPressureEntity
import ru.vt.domain.measurement.entity.BloodPressureMinMaxPeriodEntity

interface BloodPressureRepository {
    suspend fun save(entity: BloodPressureEntity)
    suspend fun getLatest(profileId: Long): BloodPressureEntity?
    suspend fun getForPeriod(
        profileId: Long,
        from: Long,
        to: Long,
        offset: Int,
        limit: Int
    ): List<BloodPressureEntity>

    suspend fun getMinMaxForPeriod(
        profileId: Long,
        bounds: List<Pair<Long, Long>>
    ): List<BloodPressureMinMaxPeriodEntity>

    suspend fun delete(profileId: Long, timestamp: Long): Boolean
}
