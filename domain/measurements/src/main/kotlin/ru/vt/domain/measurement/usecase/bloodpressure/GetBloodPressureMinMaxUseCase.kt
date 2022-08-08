package ru.vt.domain.measurement.usecase.bloodpressure

import ru.vt.core.common.UseCase
import ru.vt.domain.measurement.entity.BloodPressureMinMaxPeriodEntity
import ru.vt.domain.measurement.repository.BloodPressureRepository

class GetBloodPressureMinMaxUseCase(
    private val repository: BloodPressureRepository
) : UseCase<GetBloodPressureMinMaxUseCase.Params, List<BloodPressureMinMaxPeriodEntity>>() {

    override suspend fun execute(params: Params): Result<List<BloodPressureMinMaxPeriodEntity>> =
        Result.success(repository.getMinMaxForPeriod(params.profileId, params.bounds))

    data class Params(val profileId: Long, val bounds: List<Pair<Long, Long>>)
}
