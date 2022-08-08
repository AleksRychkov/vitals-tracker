package ru.vt.domain.dashboard.usecase

import ru.vt.core.common.UseCase
import ru.vt.domain.dashboard.entity.DashboardEntity
import ru.vt.domain.dashboard.entity.TrackedEntity
import ru.vt.domain.dashboard.repository.DashboardRepository

class UpdateTrackedItemsUseCase(
    private val repository: DashboardRepository
) : UseCase<UpdateTrackedItemsUseCase.Params, DashboardEntity>() {

    override suspend fun execute(params: Params): Result<DashboardEntity> {
        return Result.success(repository.updateTrackedItems(params.profileId, params.trackedItems))
    }

    data class Params(val profileId: Long, val trackedItems: List<TrackedEntity>)
}
