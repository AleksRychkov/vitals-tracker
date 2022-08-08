package ru.vt.data.repositories.utils

import android.util.Log
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneId
import ru.vt.data.datasource.measurements.BloodPressureDataSource
import ru.vt.domain.measurement.entity.BloodPressureEntity
import java.util.*
import java.util.concurrent.TimeUnit

internal object PrepopulateMeasurements {

    suspend fun fillToday(dataSource: BloodPressureDataSource) {
        val now = LocalDateTime.now()
        val startTime = now.toLocalTime().with(LocalTime.MIN)
        val tmp = LocalDateTime.of(now.toLocalDate(), startTime)
        val tmpTimestamp = tmp.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        (0..48).map { index ->
            BloodPressureEntity(
                profileId = 1,
                timestamp = tmpTimestamp
                        + TimeUnit.MINUTES.toMillis(30) * index
                        + TimeUnit.MINUTES.toMillis(
                    Random().nextInt(10).toLong()
                ),
                systolic = 100 + Random().nextInt(50),
                diastolic = 70 + Random().nextInt(80),
                heartRate = 20 + Random().nextInt(80)
            )
        }.forEach {
            try {
                dataSource.save(it)
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }

    suspend fun fillCurrentWeek(dataSource: BloodPressureDataSource) {
        val startTime = LocalDateTime.now().with(DayOfWeek.MONDAY).with(LocalTime.MIN)
        val tmpTimestamp = startTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        (0..7).map { day ->
            (0..48).map { index ->
                BloodPressureEntity(
                    profileId = 1,
                    timestamp = tmpTimestamp
                            + TimeUnit.DAYS.toMillis(1) * day
                            + TimeUnit.MINUTES.toMillis(30) * index
                            + TimeUnit.MINUTES.toMillis(
                        Random().nextInt(10).toLong()
                    ),
                    systolic = 100 + Random().nextInt(50),
                    diastolic = 70 + Random().nextInt(80),
                    heartRate = 20 + Random().nextInt(80)
                )
            }.forEach {
                try {
                    dataSource.save(it)
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
            }
        }
    }

    suspend fun fillCurrentMonth(dataSource: BloodPressureDataSource) {
        val startTime = LocalDateTime.now().withDayOfMonth(1).with(LocalTime.MIN)
        val tmpTimestamp = startTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()


        (0..31).map { day ->
            (0..48).map { index ->
                BloodPressureEntity(
                    profileId = 1,
                    timestamp = tmpTimestamp
                            + TimeUnit.DAYS.toMillis(1) * day
                            + TimeUnit.MINUTES.toMillis(30) * index
                            + TimeUnit.MINUTES.toMillis(
                        Random().nextInt(10).toLong()
                    ),
                    systolic = 100 + Random().nextInt(50),
                    diastolic = 70 + Random().nextInt(80),
                    heartRate = 20 + Random().nextInt(80)
                )
            }.forEach {
                try {
                    dataSource.save(it)
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
            }
        }
    }

    suspend fun fillCurrentYear(dataSource: BloodPressureDataSource) {
        val startTime = LocalDateTime.now().withDayOfYear(1).with(LocalTime.MIN)
        val tmpTimestamp = startTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        (0..365).map { day ->
            (0..48).map { index ->
                BloodPressureEntity(
                    profileId = 1,
                    timestamp = tmpTimestamp
                            + TimeUnit.DAYS.toMillis(1) * day
                            + TimeUnit.MINUTES.toMillis(30) * index
                            + TimeUnit.MINUTES.toMillis(
                        Random().nextInt(10).toLong()
                    ),
                    systolic = 110 + Random().nextInt(50),
                    diastolic = 65 + Random().nextInt(30),
                    heartRate = 80 + Random().nextInt(10)
                )
            }.forEach {
                try {
                    dataSource.save(it)
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
            }
            Log.d("[PrepopulateMeasurements]", "Progress: $day out of 365")
        }

        Log.d("[PrepopulateMeasurements]", "DONE Prepopulate DB!")
    }
}