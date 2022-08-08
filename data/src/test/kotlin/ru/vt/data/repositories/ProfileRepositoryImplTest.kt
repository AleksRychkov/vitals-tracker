package ru.vt.data.repositories

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import ru.vt.data.datasource.ProfileDataSource
import ru.vt.data.providers.ProfileProvider
import ru.vt.domain.profile.entity.ProfileEntity

@ExtendWith(MockKExtension::class)
internal class ProfileRepositoryImplTest {

    @MockK
    private lateinit var profileDataSource: ProfileDataSource

    @MockK(relaxed = true)
    private lateinit var profileProvider: ProfileProvider

    @InjectMockKs
    private lateinit var repositoryImpl: ProfileRepositoryImpl

    private val entity: ProfileEntity = ProfileEntity(
        id = 1,
        name = ""
    )

    @Nested
    inner class SaveProfile {
        @Test
        fun `should set default profile when saving new profile`(): Unit = runBlocking {
            coEvery { profileDataSource.getProfilesSize() } returns 1
            coEvery { profileDataSource.saveProfile(any()) } returns 1L

            repositoryImpl.saveProfile(entity)

            coVerify(exactly = 1) { profileProvider.defaultProfileId = any() }
        }

        @Test
        fun `should not set default profile when saving new profile`(): Unit = runBlocking {
            coEvery { profileDataSource.getProfilesSize() } returns 2
            coEvery { profileDataSource.saveProfile(any()) } returns 1L

            repositoryImpl.saveProfile(entity)

            coVerify(exactly = 0) { profileProvider.defaultProfileId = any() }
        }
    }

    @Nested
    inner class DeleteProfile {
        @Test
        fun `should reset default profile when deleting profile`(): Unit = runBlocking {
            val id = 1L
            coEvery { profileDataSource.deleteProfile(id) } returns Unit
            coEvery { profileProvider.defaultProfileId } returns id
            coEvery { profileProvider.defaultProfileId = null } returns Unit

            repositoryImpl.deleteProfile(id)

            coVerify(exactly = 1) { profileProvider.defaultProfileId = null }
        }

        @Test
        fun `should not reset default profile when deleting profile`(): Unit = runBlocking {
            val id = 1L
            coEvery { profileDataSource.deleteProfile(id) } returns Unit
            coEvery { profileProvider.defaultProfileId } returns id + 1

            repositoryImpl.deleteProfile(id)

            coVerify(exactly = 0) { profileProvider.defaultProfileId = null }
        }
    }

    @Nested
    inner class DefaultProfile {

        @Test
        fun `should return default profile`(): Unit = runBlocking {
            val profileId = 123L
            val defaultProfile = entity.copy(id = profileId)

            coEvery { profileProvider.defaultProfileId } returns profileId
            coEvery { profileDataSource.getProfileById(profileId) } returns defaultProfile
            coEvery { profileDataSource.getProfilesSize() } returns 1

            val actual = repositoryImpl.getDefaultProfile()

            coVerify(exactly = 0) { profileProvider.defaultProfileId = any() }

            assertEquals(defaultProfile, actual)
        }

        @Test
        fun `should return NULL when default profile id not set and no profiles stored in DB`(): Unit =
            runBlocking {
                coEvery { profileProvider.defaultProfileId } returns null
                coEvery { profileDataSource.getProfilesSize() } returns 0


                val actual = repositoryImpl.getDefaultProfile()

                coVerify(exactly = 0) { profileDataSource.getProfileById(any()) }
                coVerify(exactly = 0) { profileProvider.defaultProfileId = any() }
                coVerify(exactly = 0) { profileDataSource.getProfiles() }

                assertNull(actual)
            }

        @Test
        fun `should return NULL when default profile not found but no profiles stored in DB`(): Unit =
            runBlocking {
                val profileId = 123L
                coEvery { profileProvider.defaultProfileId } returns profileId
                coEvery { profileDataSource.getProfileById(profileId) } returns null
                coEvery { profileProvider.defaultProfileId = null } returns Unit
                coEvery { profileDataSource.getProfilesSize() } returns 0

                val actual = repositoryImpl.getDefaultProfile()

                coVerify(exactly = 1) { profileDataSource.getProfileById(any()) }
                coVerify(exactly = 1) { profileProvider.defaultProfileId = null }
                coVerify(exactly = 0) { profileDataSource.getProfiles() }

                assertNull(actual)
            }

        @Test
        fun `should return first stored profile when default profile id not set`(): Unit =
            runBlocking {
                val profileId = 123L
                val defaultProfile = entity.copy(id = profileId)

                coEvery { profileProvider.defaultProfileId } returns null
                coEvery { profileDataSource.getProfilesSize() } returns 1
                coEvery { profileDataSource.getProfiles() } returns listOf(defaultProfile)
                coEvery { profileProvider.defaultProfileId = profileId } returns Unit

                val actual = repositoryImpl.getDefaultProfile()

                coVerify(exactly = 1) { profileProvider.defaultProfileId = profileId }

                assertEquals(defaultProfile, actual)
            }

        @Test
        fun `should return first stored profile when default not found`(): Unit = runBlocking {
            runBlocking {
                val profileId = 123L
                val defaultProfile = entity.copy(id = profileId)

                coEvery { profileProvider.defaultProfileId } returns profileId + 1
                coEvery { profileDataSource.getProfileById(profileId + 1) } returns null
                coEvery { profileDataSource.getProfilesSize() } returns 1
                coEvery { profileDataSource.getProfiles() } returns listOf(defaultProfile)
                coEvery { profileProvider.defaultProfileId = any() } returns Unit

                val actual = repositoryImpl.getDefaultProfile()

                coVerify(exactly = 1) { profileProvider.defaultProfileId = profileId }
                coVerify(exactly = 1) { profileProvider.defaultProfileId = null }

                assertEquals(defaultProfile, actual)
            }
        }
    }
}
