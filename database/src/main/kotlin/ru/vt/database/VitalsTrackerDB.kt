package ru.vt.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.vt.database.dao.*
import ru.vt.database.entity.*

@Database(
    entities = [
        ProfileEntityDB::class,
        DashboardEntityDb::class,
        BloodPressureEntityDb::class,
        HeadacheEntityDb::class,
        WeightEntityDb::class
    ],
    version = Common.DATABASE_VERSION,
    exportSchema = true
)
abstract class VitalsTrackerDB : RoomDatabase() {
    companion object {
        fun instantiate(application: Context): VitalsTrackerDB {
            val builder = Room
                .databaseBuilder(application, VitalsTrackerDB::class.java, Common.DATABASE_NAME)
                .setJournalMode(JournalMode.TRUNCATE)
            if (BuildConfig.DEBUG) {
                builder.fallbackToDestructiveMigration()
            }
            return builder.build()
        }
    }

    abstract fun profileDao(): ProfileDao

    abstract fun dashboardDao(): DashboardDao

    abstract fun bloodPressureDao(): BloodPressureDao

    abstract fun headacheDao(): HeadacheDao

    abstract fun weightDao(): WeightDao
}
