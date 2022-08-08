package ru.vt.domain.measurement.usecase.weight

import ru.vt.core.common.UseCase
import ru.vt.domain.common.MeasurementParams
import ru.vt.domain.measurement.entity.WeightEntity
import ru.vt.domain.measurement.exceptions.EmptyValueException
import ru.vt.domain.measurement.repository.WeightRepository

class AddWeightUseCase(
    private val repository: WeightRepository
) : UseCase<AddWeightUseCase.Params, Boolean>() {

    override suspend fun execute(params: Params): Result<Boolean> {
        if (params.weightInGrams == null) {
            throw EmptyValueException(key = MeasurementParams.WEIGHT.key)
        }

        repository.save(
            WeightEntity(
                profileId = params.profileId,
                timestamp = params.timestamp,
                weightInGrams = params.weightInGrams
            )
        )
        return Result.success(true)
    }

    data class Params(
        val profileId: Long,
        val timestamp: Long,
        val weightInGrams: Int?
    )
}
