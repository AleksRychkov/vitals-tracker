package ru.vt.data.repositories

import ru.vt.data.datasource.measurements.WeightDataSource
import ru.vt.domain.measurement.entity.WeightEntity
import ru.vt.domain.measurement.entity.WeightMinMaxPeriodEntity
import ru.vt.domain.measurement.repository.WeightRepository

internal class WeightRepositoryImpl(
    private val dataSource: WeightDataSource
) : WeightRepository {
    override suspend fun save(entity: WeightEntity) {
        dataSource.save(entity)
    }

    override suspend fun getLatest(profileId: Long): WeightEntity? =
        dataSource.getLatest(profileId)

    override suspend fun getForPeriod(
        profileId: Long,
        from: Long,
        to: Long,
        offset: Int,
        limit: Int
    ): List<WeightEntity> = dataSource.getForPeriod(profileId, from, to, offset, limit)

    override suspend fun delete(profileId: Long, timestamp: Long): Boolean =
        dataSource.delete(profileId, timestamp)

    override suspend fun getMinMaxForPeriod(
        profileId: Long,
        bounds: List<Pair<Long, Long>>
    ): List<WeightMinMaxPeriodEntity> = dataSource.getMinMaxForPeriod(profileId, bounds)
}
