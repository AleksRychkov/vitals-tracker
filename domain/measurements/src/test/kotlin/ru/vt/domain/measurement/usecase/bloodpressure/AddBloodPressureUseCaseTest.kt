package ru.vt.domain.measurement.usecase.bloodpressure

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
import ru.vt.domain.measurement.exceptions.EmptyValueException
import ru.vt.domain.measurement.exceptions.ValueRangeException
import ru.vt.domain.measurement.repository.BloodPressureRepository
import ru.vt.domain.measurement.usecase.bloodpressure.AddBloodPressureUseCase as UseCase

@ExtendWith(MockKExtension::class)
internal class AddBloodPressureUseCaseTest {

    @MockK
    private lateinit var repository: BloodPressureRepository

    @InjectMockKs
    private lateinit var useCase: UseCase

    private val params: UseCase.Params =
        UseCase.Params(
            profileId = 0L,
            timestamp = System.currentTimeMillis(),
            systolic = 120,
            diastolic = 90,
            heartRate = 120,
        )

    @Nested
    inner class EmptyValueCheck {

        private suspend fun defaultCheck(params: UseCase.Params, key: String) {
            try {
                useCase.executeSync(params)
            } catch (e: Throwable) {
                assertTrue(e is EmptyValueException)
                assertEquals(key, (e as EmptyValueException).key)
            }
        }

        @Test
        fun `should fail when systolic param is NULL`() = runBlocking {
            val copy = params.copy(
                systolic = null
            )

            defaultCheck(copy, MeasurementParams.SYSTOLIC.key)
        }

        @Test
        fun `should fail when diastolic param is NULL`() = runBlocking {
            val copy = params.copy(
                diastolic = null
            )

            defaultCheck(copy, MeasurementParams.DIASTOLIC.key)
        }

        @Test
        fun `should success when heartRate param is NULL`() = runBlocking {
            coEvery { repository.save(any()) }.returns(Unit)

            val copy = params.copy(
                heartRate = null
            )
            assertTrue(
                useCase.executeSync(copy).isSuccess
            )
        }
    }

    @Nested
    inner class RangeCheck {

        private suspend fun rangeCheck(params: UseCase.Params, key: String) {
            try {
                useCase.executeSync(params)
            } catch (t: Throwable) {
                assertTrue(t is ValueRangeException)
                assertEquals(key, (t as ValueRangeException).key)
            }
        }

        @Test
        fun `should fail when systolic is out of range`() = runBlocking {
            val copyLess = params.copy(
                systolic = ru.vt.domain.measurement.usecase.bloodpressure.AddBloodPressureUseCase.SYSTOLIC_RANGE.first - 1
            )
            rangeCheck(copyLess, MeasurementParams.SYSTOLIC.key)

            val copyMore = params.copy(
                systolic = ru.vt.domain.measurement.usecase.bloodpressure.AddBloodPressureUseCase.SYSTOLIC_RANGE.second + 1
            )
            rangeCheck(copyMore, MeasurementParams.SYSTOLIC.key)
        }

        @Test
        fun `should fail when diastolic is out of range`() = runBlocking {
            val copyLess = params.copy(
                diastolic = ru.vt.domain.measurement.usecase.bloodpressure.AddBloodPressureUseCase.DIASTOLIC_RANGE.first - 1
            )
            rangeCheck(copyLess, MeasurementParams.DIASTOLIC.key)

            val copyMore = params.copy(
                diastolic = ru.vt.domain.measurement.usecase.bloodpressure.AddBloodPressureUseCase.DIASTOLIC_RANGE.second + 1
            )
            rangeCheck(copyMore, MeasurementParams.DIASTOLIC.key)
        }

        @Test
        fun `should fail when heartRate is out of range`() = runBlocking {
            val copyLess = params.copy(
                heartRate = ru.vt.domain.measurement.usecase.bloodpressure.AddBloodPressureUseCase.HEART_RATE.first - 1
            )
            rangeCheck(copyLess, MeasurementParams.HEART_RATE.key)

            val copyMore = params.copy(
                heartRate = ru.vt.domain.measurement.usecase.bloodpressure.AddBloodPressureUseCase.HEART_RATE.second + 1
            )
            rangeCheck(copyMore, MeasurementParams.HEART_RATE.key)
        }
    }

    @Nested
    inner class RepositoryCheck {
        @Test
        fun `should fail if repository fails to put new item`() {
            coEvery { repository.save(any()) }.throws(IllegalArgumentException())

            assertThrows<java.lang.IllegalArgumentException> {
                runBlocking {
                    useCase.executeSync(params)
                }
            }
        }

        @Test
        fun `should return Result_isFailure when repository fails to put new item`() = runBlocking {
            coEvery { repository.save(any()) }.throws(IllegalArgumentException())

            useCase(params).collect {
                assertTrue(it.isFailure)
                assertTrue(it.exceptionOrNull()!! is java.lang.IllegalArgumentException)
            }
        }

        @Test
        fun `should success putting new item`() = runBlocking {
            coEvery { repository.save(any()) }.returns(Unit)

            assertTrue(
                useCase.executeSync(params).isSuccess
            )
        }
    }
}
