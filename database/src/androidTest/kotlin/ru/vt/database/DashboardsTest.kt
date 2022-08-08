package ru.vt.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.vt.database.dao.DashboardDao
import ru.vt.database.entity.DashboardEntityDb
import ru.vt.database.entity.ProfileEntityDB

@RunWith(AndroidJUnit4::class)
internal class DashboardsTest {
    private lateinit var db: VitalsTrackerDB
    private lateinit var dao: DashboardDao

    private val profileId = 123L

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            VitalsTrackerDB::class.java
        ).allowMainThreadQueries().build()
        dao = db.dashboardDao()
        runBlocking {
            db.profileDao().createNewProfile(ProfileEntityDB(profileId = profileId, name = ""))
        }
    }

    @Test
    fun test(): Unit = runBlocking {
        assertNull(dao.getDashboard(profileId))

        var expected = DashboardEntityDb(profileId = profileId, trackedItems = "")
        dao.saveDashboard(expected)
        assertEquals(expected, dao.getDashboard(profileId))

        expected = expected.copy(trackedItems = (1..3).joinToString())
        dao.saveDashboard(expected)
        assertEquals(expected, dao.getDashboard(profileId))
    }

    @After
    fun release() {
        db.clearAllTables()
        db.close()
    }
}
