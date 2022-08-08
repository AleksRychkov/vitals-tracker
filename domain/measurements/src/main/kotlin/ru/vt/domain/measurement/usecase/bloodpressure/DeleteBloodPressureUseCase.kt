package ru.vt.domain.measurement.usecase.bloodpressure

import ru.vt.core.common.UseCase
import ru.vt.domain.measurement.repository.BloodPressureRepository

class DeleteBloodPressureUseCase(
    private val repository: BloodPressureRepository
) : UseCase<DeleteBloodPressureUseCase.Params, Boolean>() {

    override suspend fun execute(params: Params): Result<Boolean> =
        Result.success(repository.delete(params.profileId, params.timestamp))

    data class Params(val profileId: Long, val timestamp: Long)
}
