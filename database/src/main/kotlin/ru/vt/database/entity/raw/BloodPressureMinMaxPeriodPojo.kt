package ru.vt.database.entity.raw

import androidx.room.ColumnInfo

data class BloodPressureMinMaxPeriodPojo(
    @ColumnInfo(name = "from_bound")
    val from: Long,
    @ColumnInfo(name = "to_bound")
    val to: Long,
    @ColumnInfo(name = "systolic_max")
    val systolicMax: Int,
    @ColumnInfo(name = "systolic_min")
    val systolicMin: Int,
    @ColumnInfo(name = "diastolic_max")
    val diastolicMax: Int,
    @ColumnInfo(name = "diastolic_min")
    val diastolicMin: Int,
    @ColumnInfo(name = "heart_rate_max")
    val heartRateMax: Int? = null,
    @ColumnInfo(name = "heart_rate_min")
    val heartRateMin: Int? = null
)