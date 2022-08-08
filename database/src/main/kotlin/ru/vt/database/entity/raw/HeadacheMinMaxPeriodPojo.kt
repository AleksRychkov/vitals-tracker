package ru.vt.database.entity.raw

import androidx.room.ColumnInfo

data class HeadacheMinMaxPeriodPojo(
    @ColumnInfo(name = "from_bound")
    val from: Long,
    @ColumnInfo(name = "to_bound")
    val to: Long,
    @ColumnInfo(name = "intensity_max")
    val intensityMax: Int,
    @ColumnInfo(name = "intensity_min")
    val intensityMin: Int
)
