package ru.vt.domain.measurement.usecase.headache

import ru.vt.core.common.UseCase
import ru.vt.domain.measurement.entity.HeadacheEntity
import ru.vt.domain.measurement.exceptions.InvalidPeriodValuesException
import ru.vt.domain.measurement.repository.HeadacheRepository

class GetHeadacheUseCase(
    private val repository: HeadacheRepository
) : UseCase<GetHeadacheUseCase.Params, List<HeadacheEntity>>() {

    override suspend fun execute(params: Params): Result<List<HeadacheEntity>> {
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
