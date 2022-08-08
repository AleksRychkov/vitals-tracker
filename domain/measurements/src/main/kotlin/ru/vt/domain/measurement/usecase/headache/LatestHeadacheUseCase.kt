package ru.vt.domain.measurement.usecase.headache

import ru.vt.core.common.UseCase
import ru.vt.domain.measurement.entity.HeadacheEntity
import ru.vt.domain.measurement.repository.HeadacheRepository

class LatestHeadacheUseCase(
    private val repository: HeadacheRepository
) : UseCase<LatestHeadacheUseCase.Params, HeadacheEntity?>() {

    override suspend fun execute(params: Params): Result<HeadacheEntity?> =
        Result.success(repository.getLatest(params.profileId))

    data class Params(val profileId: Long)
}
