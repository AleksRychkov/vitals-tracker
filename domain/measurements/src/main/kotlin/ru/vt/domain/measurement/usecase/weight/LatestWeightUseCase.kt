package ru.vt.domain.measurement.usecase.weight

import ru.vt.core.common.UseCase
import ru.vt.domain.measurement.entity.WeightEntity
import ru.vt.domain.measurement.repository.WeightRepository

class LatestWeightUseCase(
    private val repository: WeightRepository
) : UseCase<LatestWeightUseCase.Params, WeightEntity?>() {

    override suspend fun execute(params: Params): Result<WeightEntity?> =
        Result.success(repository.getLatest(params.profileId))

    data class Params(val profileId: Long)
}
