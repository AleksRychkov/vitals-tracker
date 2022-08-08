package ru.vt.domain.measurement.usecase.headache

import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import ru.vt.domain.common.MeasurementParams
import ru.vt.domain.measurement.entity.HeadacheArea
import ru.vt.domain.measurement.exceptions.EmptyValueException
import ru.vt.domain.measurement.repository.HeadacheRepository

@ExtendWith(MockKExtension::class)
internal class AddHeadacheUseCaseTest {

    @MockK
    private lateinit var repository: HeadacheRepository

    @InjectMockKs
    private lateinit var useCase: AddHeadacheUseCase

    private val params: AddHeadacheUseCase.Params = AddHeadacheUseCase.Params(
        profileId = 0,
        timestamp = System.currentTimeMillis(),
        intensity = 1,
        area = HeadacheArea.UNDEFINED,
        description = ""
    )

    @Nested
    inner class EmptyValueCheck {

        @Test
        fun `should fail when intensity param is NULL`(): Unit = runBlocking {
            val copy = params.copy(
                intensity = null
            )

            try {
                useCase.executeSync(copy)
            } catch (e: Throwable) {
                assertTrue(e is EmptyValueException)
                assertEquals(
                    MeasurementParams.HEADACHE_INTENSITY.key,
                    (e as EmptyValueException).key
                )
            }
        }
    }

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
                assertTrue(it.isFailure)
                assertTrue(it.exceptionOrNull()!! is IllegalStateException)
            }
        }

        @Test
        fun `should success saving`() = runBlocking {
            coEvery { repository.save(any()) }.returns(Unit)

            assertTrue(
                useCase.executeSync(params).isSuccess
            )
        }
    }
}