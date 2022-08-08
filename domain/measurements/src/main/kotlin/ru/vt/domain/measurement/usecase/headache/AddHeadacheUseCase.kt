package ru.vt.domain.measurement.usecase.headache

import ru.vt.core.common.UseCase
import ru.vt.domain.common.MeasurementParams
import ru.vt.domain.measurement.entity.HeadacheArea
import ru.vt.domain.measurement.entity.HeadacheEntity
import ru.vt.domain.measurement.exceptions.EmptyValueException
import ru.vt.domain.measurement.repository.HeadacheRepository

class AddHeadacheUseCase(
    private val repository: HeadacheRepository
) : UseCase<AddHeadacheUseCase.Params, Boolean>() {

    override suspend fun execute(params: Params): Result<Boolean> {
        if (params.intensity == null) {
            throw EmptyValueException(key = MeasurementParams.HEADACHE_INTENSITY.key)
        }
        repository.save(
            HeadacheEntity(
                profileId = params.profileId,
                timestamp = params.timestamp,
                intensity = params.intensity,
                headacheArea = params.area,
                description = params.description
            )
        )
        return Result.success(true)
    }

    data class Params(
        val profileId: Long,
        val timestamp: Long,
        val intensity: Int? = null,
        val area: HeadacheArea,
        val description: String?
    )
}
