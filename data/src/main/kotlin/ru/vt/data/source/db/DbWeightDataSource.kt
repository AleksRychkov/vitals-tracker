package ru.vt.data.source.db

import androidx.annotation.VisibleForTesting
import androidx.sqlite.db.SimpleSQLiteQuery
import ru.vt.data.datasource.measurements.WeightDataSource
import ru.vt.database.VitalsTrackerDB
import ru.vt.database.entity.WeightEntityDb
import ru.vt.database.entity.raw.WeightMinMaxPeriodPojo
import ru.vt.domain.measurement.entity.WeightEntity
import ru.vt.domain.measurement.entity.WeightMinMaxPeriodEntity

internal class DbWeightDataSource(
    private val db: VitalsTrackerDB
) : WeightDataSource {

    private companion object {
        const val PERIOD_FROM_PLACEHOLDER = "{period_from}"
        const val PERIOD_TO_PLACEHOLDER = "{period_to}"
        const val PROFILE_ID_PLACEHOLDER = "{profile_id}"
        const val TIMESTAMP_FROM_PLACEHOLDER = "{from}"
        const val TIMESTAMP_TO_PLACEHOLDER = "{to}"

        const val MIN_MAX_QUERY = """
            SELECT $PERIOD_FROM_PLACEHOLDER as from_bound, $PERIOD_TO_PLACEHOLDER as to_bound,
                MAX(weight) as weight_max, MIN(weight) as weight_min
            FROM weight
            WHERE profile_id = $PROFILE_ID_PLACEHOLDER AND
                timestamp >= $TIMESTAMP_FROM_PLACEHOLDER AND
                timestamp < $TIMESTAMP_TO_PLACEHOLDER
            """
        const val UNION_QUERY = """ 
            UNION
        """
    }

    override suspend fun save(entity: WeightEntity) {
        db.weightDao().save(Mapper.entityToDb(entity))
    }

    override suspend fun getLatest(profileId: Long): WeightEntity? =
        db.weightDao().getLatest(profileId)?.let { Mapper.dbToEntity(it) }

    override suspend fun getForPeriod(
        profileId: Long,
        from: Long,
        to: Long,
        offset: Int,
        limit: Int
    ): List<WeightEntity> =
        db.weightDao().getForPeriod(profileId, from, to, offset, limit).map {
            Mapper.dbToEntity(it)
        }

    override suspend fun delete(profileId: Long, timestamp: Long): Boolean =
        db.weightDao().delete(profileId, timestamp) == 1

    override suspend fun getMinMaxForPeriod(
        profileId: Long,
        bounds: List<Pair<Long, Long>>
    ): List<WeightMinMaxPeriodEntity> {
        val sb = StringBuilder()
        for (bound in bounds) {
            if (sb.isNotEmpty()) {
                sb.append(UNION_QUERY)
            }
            val query = MIN_MAX_QUERY
                .replace(PERIOD_FROM_PLACEHOLDER, bound.first.toString())
                .replace(PERIOD_TO_PLACEHOLDER, bound.second.toString())
                .replace(PROFILE_ID_PLACEHOLDER, profileId.toString())
                .replace(TIMESTAMP_FROM_PLACEHOLDER, bound.first.toString())
                .replace(TIMESTAMP_TO_PLACEHOLDER, bound.second.toString())
            sb.append(query)
        }
        return db.weightDao()
            .getMinMaxPeriod(SimpleSQLiteQuery(sb.toString()))
            .map { Mapper.dbToEntity(it) }
    }

    @VisibleForTesting
    object Mapper {
        fun entityToDb(entity: WeightEntity): WeightEntityDb =
            WeightEntityDb(
                id = 0,
                profileId = entity.profileId,
                timestamp = entity.timestamp,
                weight = entity.weightInGrams
            )

        fun dbToEntity(db: WeightEntityDb): WeightEntity = WeightEntity(
            profileId = db.profileId,
            timestamp = db.timestamp,
            weightInGrams = db.weight
        )

        fun dbToEntity(db: WeightMinMaxPeriodPojo): WeightMinMaxPeriodEntity =
            WeightMinMaxPeriodEntity(
                bound = db.from to db.to,
                weightMax = db.weightMax,
                weightMin = db.weightMin
            )
    }
}
