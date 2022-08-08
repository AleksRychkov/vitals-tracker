package ru.vt.data.repositories

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.vt.data.datasource.measurements.BloodPressureDataSource
import ru.vt.data.repositories.utils.PrepopulateMeasurements
import ru.vt.domain.measurement.entity.BloodPressureEntity
import ru.vt.domain.measurement.entity.BloodPressureMinMaxPeriodEntity
import ru.vt.domain.measurement.repository.BloodPressureRepository

@OptIn(DelicateCoroutinesApi::class)
internal class BloodPressureRepositoryImpl(
    private val dataSource: BloodPressureDataSource
) : BloodPressureRepository {

    init {
        GlobalScope.launch(Dispatchers.IO) {
            PrepopulateMeasurements.fillCurrentYear(dataSource = dataSource)
        }
    }

    override suspend fun save(entity: BloodPressureEntity) {
        dataSource.save(entity)
    }

    override suspend fun getLatest(profileId: Long): BloodPressureEntity? =
        dataSource.getLatest(profileId)

    override suspend fun getForPeriod(
        profileId: Long,
        from: Long,
        to: Long,
        offset: Int,
        limit: Int
    ): List<BloodPressureEntity> = dataSource.get(profileId, from, to, offset, limit)

    override suspend fun getMinMaxForPeriod(
        profileId: Long,
        bounds: List<Pair<Long, Long>>
    ): List<BloodPressureMinMaxPeriodEntity> = dataSource.getMinMaxPeriod(profileId, bounds)

    override suspend fun delete(profileId: Long, timestamp: Long): Boolean =
        dataSource.delete(profileId, timestamp)
}
