package ru.vt.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.vt.database.Common

@Entity(
    tableName = Common.TABLE_PROFILE,
    indices = [Index(value = ["name"], unique = true)]
)
data class ProfileEntityDB(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "profile_id")
    val profileId: Long = 0,
    val name: String,
    @ColumnInfo(name = "birth_day")
    val birthDay: Int? = null,
    @ColumnInfo(name = "birth_month")
    val birthMonth: Int? = null,
    @ColumnInfo(name = "birth_year")
    val birthYear: Int? = null,
    val gender: String? = null,
    @ColumnInfo(name = "height_cm")
    val heightCm: Int? = null,
    @ColumnInfo(name = "weight_g")
    val weightG: Int? = null
)
