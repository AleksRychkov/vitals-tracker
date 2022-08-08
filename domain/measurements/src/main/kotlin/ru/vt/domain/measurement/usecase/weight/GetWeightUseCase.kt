package ru.vt.domain.measurement.usecase.weight

import ru.vt.core.common.UseCase
import ru.vt.domain.measurement.entity.WeightEntity
import ru.vt.domain.measurement.exceptions.InvalidPeriodValuesException
import ru.vt.domain.measurement.repository.WeightRepository

class GetWeightUseCase(
    private val repository: WeightRepository
) : UseCase<GetWeightUseCase.Params, List<WeightEntity>>() {

    override suspend fun execute(params: Params): Result<List<WeightEntity>> {
        if (params.from >= params.to) {
            throw InvalidPeriodValuesException
        }

        return Result.success(
            repository.getForPeriod(
                profileId = params.profileId,
                from = params.from,
                to = params.to,
                offset = params.offset,
                limit = params.limit
            )
        )
    }

    data class Params(
        val profileId: Long,
        val from: Long,
        val to: Long,
        val offset: Int,
        val limit: Int
    )
}
