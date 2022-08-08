package ru.vt.data.datasource.measurements

import ru.vt.domain.measurement.entity.HeadacheEntity
import ru.vt.domain.measurement.entity.HeadacheMinMaxPeriodEntity

internal interface HeadacheDataSource {
    suspend fun get(
        profileId: Long,
        from: Long,
        to: Long,
        offset: Int,
        limit: Int
    ): List<HeadacheEntity>

    suspend fun save(entity: HeadacheEntity)

    suspend fun getLatest(profileId: Long): HeadacheEntity?

    suspend fun delete(profileId: Long, timestamp: Long): Boolean

    suspend fun getMinMaxPeriod(
        profileId: Long,
        bounds: List<Pair<Long, Long>>
    ): List<HeadacheMinMaxPeriodEntity>
}
