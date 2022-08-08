package ru.vt.database.entity.raw

import androidx.room.ColumnInfo

data class WeightMinMaxPeriodPojo(
    @ColumnInfo(name = "from_bound")
    val from: Long,
    @ColumnInfo(name = "to_bound")
    val to: Long,
    @ColumnInfo(name = "weight_max")
    val weightMax: Int,
    @ColumnInfo(name = "weight_min")
    val weightMin: Int
)
