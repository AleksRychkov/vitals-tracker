package ru.vt.database

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.vt.database.dao.ProfileDao
import ru.vt.database.entity.ProfileEntityDB
import java.lang.Exception

@RunWith(AndroidJUnit4::class)
internal class ProfilesTest {
    private lateinit var db: VitalsTrackerDB
    private lateinit var dao: ProfileDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            VitalsTrackerDB::class.java
        ).allowMainThreadQueries().build()
        dao = db.profileDao()
    }

    @Test
    fun createAndDeleteProfiles() = runBlocking {
        db.clearAllTables()
        val entity = ProfileEntityDB(profileId = 0, name = "Name")
        val ids = listOf(
            dao.createNewProfile(entity),
            dao.createNewProfile(entity.copy(name = "Name2"))
        )
        val profilesSize = dao.getProfilesSize()
        assertTrue(profilesSize == ids.size)

        dao.deleteProfile(ids.first())
        assertTrue(dao.getProfilesSize() == 1)

        dao.deleteProfile(ids[1])
        assertTrue(dao.getProfilesSize() == 0)
    }

    @Test
    fun uniqueName() = runBlocking {
        db.clearAllTables()
        val entity = ProfileEntityDB(profileId = 0, name = "Name")

        dao.createNewProfile(entity)
        try {
            dao.createNewProfile(entity)
        } catch (t: Exception) {
            assertTrue(t is SQLiteConstraintException)
        }

        val profilesInDbSize = dao.getProfilesSize()
        assertTrue(profilesInDbSize == 1)
    }

    @Test
    fun findByName() = runBlocking {
        db.clearAllTables()
        val entity = ProfileEntityDB(profileId = 0, name = "Name")

        dao.createNewProfile(entity)
        dao.createNewProfile(entity.copy(name = "Name2"))

        val searchName = entity.name
        val profiles = dao.findByName(searchName)

        assertTrue(profiles.size == 1)
        assertTrue(profiles.first().name == searchName)
    }

    @Test
    fun findByNoneExistingId() = runBlocking {
        db.clearAllTables()
        val entity = ProfileEntityDB(profileId = 0, name = "Name")
        val profileId = dao.createNewProfile(entity)

        val profile = dao.getProfileById(profileId + 123L)
        assertNull(profile)
    }

    @After
    fun release() {
        db.clearAllTables()
        db.close()
    }
}