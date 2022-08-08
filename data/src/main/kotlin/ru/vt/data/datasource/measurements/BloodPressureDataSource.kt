package ru.vt.data.datasource.measurements

import ru.vt.domain.measurement.entity.BloodPressureEntity
import ru.vt.domain.measurement.entity.BloodPressureMinMaxPeriodEntity

internal interface BloodPressureDataSource {

    suspend fun get(
        profileId: Long,
        from: Long,
        to: Long,
        offset: Int,
        limit: Int
    ): List<BloodPressureEntity>

    suspend fun save(entity: BloodPressureEntity)

    suspend fun getLatest(profileId: Long): BloodPressureEntity?

    suspend fun delete(profileId: Long, timestamp: Long): Boolean

    suspend fun clear()

    suspend fun getMinMaxPeriod(
        profileId: Long, bounds: List<Pair<Long, Long>>
    ): List<BloodPressureMinMaxPeriodEntity>
}
