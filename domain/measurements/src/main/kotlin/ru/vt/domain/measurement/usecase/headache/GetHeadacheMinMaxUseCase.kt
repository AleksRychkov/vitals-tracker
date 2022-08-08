package ru.vt.domain.measurement.usecase.headache

import ru.vt.core.common.UseCase
import ru.vt.domain.measurement.entity.HeadacheMinMaxPeriodEntity
import ru.vt.domain.measurement.repository.HeadacheRepository

class GetHeadacheMinMaxUseCase(
    private val repository: HeadacheRepository
) : UseCase<GetHeadacheMinMaxUseCase.Params, List<HeadacheMinMaxPeriodEntity>>() {

    override suspend fun execute(params: Params): Result<List<HeadacheMinMaxPeriodEntity>> =
        Result.success(repository.getMinMaxForPeriod(params.profileId, params.bounds))

    data class Params(val profileId: Long, val bounds: List<Pair<Long, Long>>)
}
