package ru.vt.data.datasource.measurements

import ru.vt.domain.measurement.entity.WeightEntity
import ru.vt.domain.measurement.entity.WeightMinMaxPeriodEntity

interface WeightDataSource {
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