package ru.vt.domain.profile.usecase

import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import ru.vt.domain.common.SimpleDateEntity
import ru.vt.domain.profile.exceptions.EmptyNameError
import ru.vt.domain.profile.exceptions.UniqueNameError
import ru.vt.domain.profile.repository.ProfileRepository

@ExtendWith(MockKExtension::class)
internal class SaveProfileUseCaseTest {

    @MockK
    private lateinit var repository: ProfileRepository

    @InjectMockKs
    private lateinit var useCase: SaveProfileUseCase

    private val birthdateStub = SimpleDateEntity(12, 2, 1989)

    @Test
    fun `should fail with EmptyNameError when saving new profile with empty name`() =
        runBlocking {
            val result = useCase.executeSync(SaveProfileUseCase.Params("", birthdateStub))
            assertTrue(result.isFailure && result.exceptionOrNull()!! is EmptyNameError)
        }

    @Test
    fun `should fail with UniqueNameError when saving new profile with not unique name`() =
        runBlocking {
            coEvery { repository.isNameAvailable(any()) } returns false

            val result = useCase.executeSync(SaveProfileUseCase.Params("name", birthdateStub))
            assertTrue(result.isFailure && result.exceptionOrNull()!! is UniqueNameError)
        }

    @Test
    fun `should successfully save new profile`() = runBlocking {
        coEvery { repository.isNameAvailable(any()) } returns true
        coEvery { repository.saveProfile(any()) } returns 0L

        val result = useCase.executeSync(SaveProfileUseCase.Params("name", birthdateStub))
        assertTrue(result.isSuccess)
    }

    @Test
    fun `should successfully save new profile when birthday is null`() = runBlocking {
        coEvery { repository.isNameAvailable(any()) } returns true
        coEvery { repository.saveProfile(any()) } returns 0L

        val result = useCase.executeSync(SaveProfileUseCase.Params("name", null))
        assertTrue(result.isSuccess)
    }
}
