package ru.vt.domain.measurement.usecase.bloodpressure

import ru.vt.core.common.UseCase
import ru.vt.domain.common.MeasurementParams
import ru.vt.domain.measurement.entity.BloodPressureEntity
import ru.vt.domain.measurement.exceptions.EmptyValueException
import ru.vt.domain.measurement.exceptions.ValueRangeException
import ru.vt.domain.measurement.repository.BloodPressureRepository
import ru.vt.domain.measurement.MeasurementConstants as Const

class AddBloodPressureUseCase(
    private val repository: BloodPressureRepository
) : UseCase<AddBloodPressureUseCase.Params, Boolean>() {

    companion object {
        val SYSTOLIC_RANGE: Pair<Int, Int> = Const.MIN_SYSTOLIC to Const.MAX_SYSTOLIC
        val DIASTOLIC_RANGE: Pair<Int, Int> = Const.MIN_DIASTOLIC to Const.MAX_DIASTOLIC
        val HEART_RATE: Pair<Int, Int> = Const.MIN_HEART_RATE to Const.MAX_HEART_RATE
    }

    override suspend fun execute(params: Params): Result<Boolean> {
        val systolic = emptyCheck(MeasurementParams.SYSTOLIC.key, params.systolic)
        val diastolic = emptyCheck(MeasurementParams.DIASTOLIC.key, params.diastolic)

        rangeCheck(MeasurementParams.SYSTOLIC.key, systolic, SYSTOLIC_RANGE)
        rangeCheck(MeasurementParams.DIASTOLIC.key, diastolic, DIASTOLIC_RANGE)

        if (params.heartRate != null) {
            rangeCheck(MeasurementParams.HEART_RATE.key, params.heartRate, HEART_RATE)
        }

        repository.save(
            BloodPressureEntity(
                profileId = params.profileId,
                timestamp = params.timestamp,
                systolic = systolic,
                diastolic = diastolic,
                heartRate = params.heartRate
            )
        )
        return Result.success(true)
    }

    private fun emptyCheck(key: String, value: Int?): Int =
        value ?: throw EmptyValueException(key = key)

    private fun rangeCheck(key: String, value: Int, range: Pair<Int, Int>) {
        if (value < range.first || value > range.second) {
            throw ValueRangeException(key = key, range = range)
        }
    }

    data class Params(
        val profileId: Long,
        val timestamp: Long,
        val systolic: Int?,
        val diastolic: Int?,
        val heartRate: Int?
    )
}
