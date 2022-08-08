package ru.vt.domain.measurement.usecase.bloodpressure

import ru.vt.core.common.UseCase
import ru.vt.domain.measurement.entity.BloodPressureEntity
import ru.vt.domain.measurement.exceptions.InvalidPeriodValuesException
import ru.vt.domain.measurement.repository.BloodPressureRepository

class GetBloodPressureUseCase(
    private val repository: BloodPressureRepository
) : UseCase<GetBloodPressureUseCase.Params, List<BloodPressureEntity>>() {

    override suspend fun execute(params: Params): Result<List<BloodPressureEntity>> {
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
