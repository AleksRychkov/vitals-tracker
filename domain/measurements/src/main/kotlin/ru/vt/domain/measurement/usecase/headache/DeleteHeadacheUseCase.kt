package ru.vt.domain.measurement.usecase.headache

import ru.vt.core.common.UseCase
import ru.vt.domain.measurement.repository.HeadacheRepository

class DeleteHeadacheUseCase(
    private val repository: HeadacheRepository
) : UseCase<DeleteHeadacheUseCase.Params, Boolean>() {

    override suspend fun execute(params: Params): Result<Boolean> =
        Result.success(repository.delete(params.profileId, params.timestamp))

    data class Params(val profileId: Long, val timestamp: Long)
}
