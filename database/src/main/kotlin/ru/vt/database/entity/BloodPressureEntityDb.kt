package ru.vt.database.entity

import androidx.room.*
import ru.vt.database.Common

@Entity(
    tableName = Common.TABLE_BLOOD_PRESSURE,
    foreignKeys = [
        ForeignKey(
            entity = ProfileEntityDB::class,
            parentColumns = ["profile_id"],
            childColumns = ["profile_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["profile_id", "timestamp"], unique = true)]
)
data class BloodPressureEntityDb(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "profile_id")
    val profileId: Long,
    val timestamp: Long,
    val systolic: Int,
    val diastolic: Int,
    @ColumnInfo(name = "heart_rate")
    val heartRate: Int? = null
)