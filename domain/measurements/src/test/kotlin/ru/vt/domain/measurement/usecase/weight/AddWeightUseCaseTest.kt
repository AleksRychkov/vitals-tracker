package ru.vt.domain.measurement.usecase.weight

import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import ru.vt.domain.measurement.repository.WeightRepository

@ExtendWith(MockKExtension::class)
internal class AddWeightUseCaseTest {

    @MockK
    private lateinit var repository: WeightRepository

    @InjectMockKs
    private lateinit var useCase: AddWeightUseCase

    private val params: AddWeightUseCase.Params = AddWeightUseCase.Params(
        profileId = 0,
        timestamp = System.currentTimeMillis(),
        weightInGrams = 125
    )

    @Nested
    inner class RepositoryCheck {

        @Test
        fun `should fail if repository fails to save()`() {
            coEvery { repository.save(any()) }.throws(IllegalStateException())

            assertThrows<IllegalStateException> {
                runBlocking {
                    useCase.executeSync(params)
                }
            }
        }

        @Test
        fun `should return Result_isFailure when repository fails save()`() = runBlocking {
            coEvery { repository.save(any()) }.throws(IllegalStateException())

            useCase(params).collect {
                Assertions.assertTrue(it.isFailure)
                Assertions.assertTrue(it.exceptionOrNull()!! is IllegalStateException)
            }
        }

        @Test
        fun `should success saving`() = runBlocking {
            coEvery { repository.save(any()) }.returns(Unit)

            Assertions.assertTrue(
                useCase.executeSync(params).isSuccess
            )
        }
    }
}
