package ru.vt.domain.measurement.repository

import ru.vt.domain.measurement.entity.WeightEntity
import ru.vt.domain.measurement.entity.WeightMinMaxPeriodEntity

interface WeightRepository {
    suspend fun save(entity: WeightEntity)
    suspend fun getLatest(profileId: Long): WeightEntity?
    suspend fun getForPeriod(
        profileId: Long,
        from: Long,
        to: Long,
        offset: Int,
        limit: Int
    ): List<WeightEntity>

    suspend fun delete(profileId: Long, timestamp: Long): Boolean

    suspend fun getMinMaxForPeriod(
        profileId: Long,
        bounds: List<Pair<Long, Long>>
    ): List<WeightMinMaxPeriodEntity>
}