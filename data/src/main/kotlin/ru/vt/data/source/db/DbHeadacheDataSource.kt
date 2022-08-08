package ru.vt.data.source.db

import androidx.annotation.VisibleForTesting
import androidx.sqlite.db.SimpleSQLiteQuery
import ru.vt.data.datasource.measurements.HeadacheDataSource
import ru.vt.database.VitalsTrackerDB
import ru.vt.database.entity.HeadacheEntityDb
import ru.vt.database.entity.raw.HeadacheMinMaxPeriodPojo
import ru.vt.domain.measurement.entity.HeadacheArea
import ru.vt.domain.measurement.entity.HeadacheEntity
import ru.vt.domain.measurement.entity.HeadacheMinMaxPeriodEntity

internal class DbHeadacheDataSource(
    private val db: VitalsTrackerDB
) : HeadacheDataSource {

    private companion object {
        const val PERIOD_FROM_PLACEHOLDER = "{period_from}"
        const val PERIOD_TO_PLACEHOLDER = "{period_to}"
        const val PROFILE_ID_PLACEHOLDER = "{profile_id}"
        const val TIMESTAMP_FROM_PLACEHOLDER = "{from}"
        const val TIMESTAMP_TO_PLACEHOLDER = "{to}"

        const val MIN_MAX_QUERY = """
            SELECT $PERIOD_FROM_PLACEHOLDER as from_bound, $PERIOD_TO_PLACEHOLDER as to_bound,
                MAX(intensity) as intensity_max, MIN(intensity) as intensity_min
            FROM headache
            WHERE profile_id = $PROFILE_ID_PLACEHOLDER AND
                timestamp >= $TIMESTAMP_FROM_PLACEHOLDER AND
                timestamp < $TIMESTAMP_TO_PLACEHOLDER
            """
        const val UNION_QUERY = """ 
            UNION
        """
    }

    override suspend fun get(
        profileId: Long,
        from: Long,
        to: Long,
        offset: Int,
        limit: Int
    ): List<HeadacheEntity> =
        db.headacheDao().getForPeriod(profileId, from, to, offset, limit).map {
            Mapper.dbToEntity(it)
        }

    override suspend fun save(entity: HeadacheEntity) {
        db.headacheDao().save(Mapper.entityToDb(entity))
    }

    override suspend fun getLatest(profileId: Long): HeadacheEntity? =
        db.headacheDao().getLatest(profileId)?.let { Mapper.dbToEntity(it) }

    override suspend fun delete(profileId: Long, timestamp: Long): Boolean =
        db.headacheDao().delete(profileId, timestamp) == 1

    override suspend fun getMinMaxPeriod(
        profileId: Long,
        bounds: List<Pair<Long, Long>>
    ): List<HeadacheMinMaxPeriodEntity> {
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
        return db.headacheDao()
            .getMinMaxPeriod(SimpleSQLiteQuery(sb.toString()))
            .map { Mapper.dbToEntity(it) }
    }

    @VisibleForTesting
    object Mapper {
        fun entityToDb(entity: HeadacheEntity): HeadacheEntityDb =
            HeadacheEntityDb(
                id = 0,
                profileId = entity.profileId,
                timestamp = entity.timestamp,
                intensity = entity.intensity,
                headacheArea = entity.headacheArea.key,
                description = entity.description
            )

        fun dbToEntity(db: HeadacheEntityDb): HeadacheEntity =
            HeadacheEntity(
                profileId = db.profileId,
                timestamp = db.timestamp,
                intensity = db.intensity,
                headacheArea = HeadacheArea.valueFromKey(db.headacheArea),
                description = db.description
            )

        fun dbToEntity(db: HeadacheMinMaxPeriodPojo): HeadacheMinMaxPeriodEntity =
            HeadacheMinMaxPeriodEntity(
                bound = db.from to db.to,
                intensityMax = db.intensityMax,
                intensityMin = db.intensityMin
            )
    }
}
