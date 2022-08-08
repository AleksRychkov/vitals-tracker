package ru.vt.database.entity

import androidx.room.*
import ru.vt.database.Common

@Entity(
    tableName = Common.TABLE_WEIGHT,
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
data class WeightEntityDb(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "profile_id")
    val profileId: Long,
    val timestamp: Long,
    val weight: Int,
)
