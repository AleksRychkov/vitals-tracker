package ru.vt.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import ru.vt.database.Common

@Entity(
    tableName = Common.TABLE_DASHBOARD,
    foreignKeys = [
        ForeignKey(
            entity = ProfileEntityDB::class,
            parentColumns = ["profile_id"],
            childColumns = ["profile_id"],
            onDelete = CASCADE
        )
    ]
)
data class DashboardEntityDb(
    @PrimaryKey
    @ColumnInfo(name = "profile_id")
    val profileId: Long,
    @ColumnInfo(name = "tracked_items")
    val trackedItems: String
)
