package ru.vt.domain.measurement.repository

import ru.vt.domain.measurement.entity.HeadacheEntity
import ru.vt.domain.measurement.entity.HeadacheMinMaxPeriodEntity

interface HeadacheRepository {
    suspend fun save(entity: HeadacheEntity)
    suspend fun getLatest(profileId: Long): HeadacheEntity?
    suspend fun getForPeriod(
        profileId: Long,
        from: Long,
        to: Long,
        offset: Int,
        limit: Int
    ): List<HeadacheEntity>

    suspend fun delete(profileId: Long, timestamp: Long): Boolean

    suspend fun getMinMaxForPeriod(
        profileId: Long,
        bounds: List<Pair<Long, Long>>
    ): List<HeadacheMinMaxPeriodEntity>
}
