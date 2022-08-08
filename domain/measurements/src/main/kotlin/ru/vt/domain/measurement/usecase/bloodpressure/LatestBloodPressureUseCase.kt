package ru.vt.domain.measurement.usecase.bloodpressure

import ru.vt.core.common.UseCase
import ru.vt.domain.measurement.entity.BloodPressureEntity
import ru.vt.domain.measurement.repository.BloodPressureRepository

class LatestBloodPressureUseCase(
    private val repository: BloodPressureRepository
) : UseCase<LatestBloodPressureUseCase.Params, BloodPressureEntity?>() {

    override suspend fun execute(params: Params): Result<BloodPressureEntity?> =
        Result.success(repository.getLatest(params.profileId))

    data class Params(val profileId: Long)
}
