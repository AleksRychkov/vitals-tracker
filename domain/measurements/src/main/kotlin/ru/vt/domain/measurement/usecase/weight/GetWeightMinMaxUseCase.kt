package ru.vt.domain.measurement.usecase.weight

import ru.vt.core.common.UseCase
import ru.vt.domain.measurement.entity.WeightMinMaxPeriodEntity
import ru.vt.domain.measurement.repository.WeightRepository

class GetWeightMinMaxUseCase(
    private val repository: WeightRepository
) : UseCase<GetWeightMinMaxUseCase.Params, List<WeightMinMaxPeriodEntity>>() {

    override suspend fun execute(params: Params): Result<List<WeightMinMaxPeriodEntity>> =
        Result.success(repository.getMinMaxForPeriod(params.profileId, params.bounds))

    data class Params(val profileId: Long, val bounds: List<Pair<Long, Long>>)
}
