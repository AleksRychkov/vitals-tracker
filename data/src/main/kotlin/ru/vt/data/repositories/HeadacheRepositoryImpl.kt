package ru.vt.data.repositories

import kotlinx.coroutines.DelicateCoroutinesApi
import ru.vt.data.datasource.measurements.HeadacheDataSource
import ru.vt.domain.measurement.entity.HeadacheEntity
import ru.vt.domain.measurement.entity.HeadacheMinMaxPeriodEntity
import ru.vt.domain.measurement.repository.HeadacheRepository

@OptIn(DelicateCoroutinesApi::class)
internal class HeadacheRepositoryImpl(
    private val dataSource: HeadacheDataSource
) : HeadacheRepository {

    override suspend fun save(entity: HeadacheEntity) {
        dataSource.save(entity)
    }

    override suspend fun getLatest(profileId: Long): HeadacheEntity? =
        dataSource.getLatest(profileId)

    override suspend fun getForPeriod(
        profileId: Long,
        from: Long,
        to: Long,
        offset: Int,
        limit: Int
    ): List<HeadacheEntity> = dataSource.get(profileId, from, to, offset, limit)

    override suspend fun delete(profileId: Long, timestamp: Long): Boolean =
        dataSource.delete(profileId, timestamp)

    override suspend fun getMinMaxForPeriod(
        profileId: Long,
        bounds: List<Pair<Long, Long>>
    ): List<HeadacheMinMaxPeriodEntity> = dataSource.getMinMaxPeriod(profileId, bounds)
}
