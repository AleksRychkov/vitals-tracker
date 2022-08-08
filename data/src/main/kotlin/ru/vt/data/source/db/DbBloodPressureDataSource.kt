package ru.vt.data.source.db

import androidx.annotation.VisibleForTesting
import androidx.sqlite.db.SimpleSQLiteQuery
import ru.vt.data.datasource.measurements.BloodPressureDataSource
import ru.vt.database.VitalsTrackerDB
import ru.vt.database.entity.BloodPressureEntityDb
import ru.vt.database.entity.raw.BloodPressureMinMaxPeriodPojo
import ru.vt.domain.measurement.entity.BloodPressureEntity
import ru.vt.domain.measurement.entity.BloodPressureMinMaxPeriodEntity

internal class DbBloodPressureDataSource(
    private val db: VitalsTrackerDB
) : BloodPressureDataSource {

    private companion object {
        const val PERIOD_FROM_PLACEHOLDER = "{period_from}"
        const val PERIOD_TO_PLACEHOLDER = "{period_to}"
        const val PROFILE_ID_PLACEHOLDER = "{profile_id}"
        const val TIMESTAMP_FROM_PLACEHOLDER = "{from}"
        const val TIMESTAMP_TO_PLACEHOLDER = "{to}"
        const val MIN_MAX_QUERY = """
            SELECT $PERIOD_FROM_PLACEHOLDER as from_bound, $PERIOD_TO_PLACEHOLDER as to_bound,
                MAX(systolic) as systolic_max, MIN(systolic) as systolic_min,
                MAX(diastolic) as diastolic_max, MIN (diastolic) as diastolic_min,
                MAX(heart_rate) as heart_rate_max, MIN (heart_rate) as heart_rate_min
            FROM blood_pressure
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
    ): List<BloodPressureEntity> =
        db.bloodPressureDao().getForPeriod(profileId, from, to, offset, limit).map {
            Mapper.dbToEntity(it)
        }

    override suspend fun save(entity: BloodPressureEntity) {
        db.bloodPressureDao().save(Mapper.entityToDb(entity))
    }

    override suspend fun getLatest(profileId: Long): BloodPressureEntity? =
        db.bloodPressureDao().getLatest(profileId)?.let { Mapper.dbToEntity(it) }

    override suspend fun delete(profileId: Long, timestamp: Long): Boolean =
        db.bloodPressureDao().delete(profileId, timestamp) == 1

    override suspend fun clear() {
        db.bloodPressureDao().clear()
    }

    override suspend fun getMinMaxPeriod(
        profileId: Long,
        bounds: List<Pair<Long, Long>>
    ): List<BloodPressureMinMaxPeriodEntity> {
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
        return db.bloodPressureDao()
            .getMinMaxPeriod(SimpleSQLiteQuery(sb.toString()))
            .map { Mapper.dbToEntity(it) }
    }

    @VisibleForTesting
    object Mapper {
        fun entityToDb(entity: BloodPressureEntity): BloodPressureEntityDb =
            BloodPressureEntityDb(
                id = 0,
                profileId = entity.profileId,
                timestamp = entity.timestamp,
                systolic = entity.systolic,
                diastolic = entity.diastolic,
                heartRate = entity.heartRate
            )

        fun dbToEntity(db: BloodPressureEntityDb): BloodPressureEntity =
            BloodPressureEntity(
                profileId = db.profileId,
                timestamp = db.timestamp,
                systolic = db.systolic,
                diastolic = db.diastolic,
                heartRate = db.heartRate
            )

        fun dbToEntity(db: BloodPressureMinMaxPeriodPojo): BloodPressureMinMaxPeriodEntity =
            BloodPressureMinMaxPeriodEntity(
                bound = db.from to db.to,
                systolicMax = db.systolicMax,
                systolicMin = db.systolicMin,
                diastolicMax = db.diastolicMax,
                diastolicMin = db.diastolicMin,
                heartRateMax = db.heartRateMax,
                heartRateMin = db.heartRateMin
            )
    }
}
