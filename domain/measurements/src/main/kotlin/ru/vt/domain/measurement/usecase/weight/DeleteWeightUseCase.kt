package ru.vt.domain.measurement.usecase.weight

import ru.vt.core.common.UseCase
import ru.vt.domain.measurement.repository.WeightRepository

class DeleteWeightUseCase(
    private val repository: WeightRepository
) : UseCase<DeleteWeightUseCase.Params, Boolean>() {

    override suspend fun execute(params: Params): Result<Boolean> =
        Result.success(repository.delete(params.profileId, params.timestamp))

    data class Params(val profileId: Long, val timestamp: Long)
}
