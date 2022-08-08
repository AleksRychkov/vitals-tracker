package ru.vt.domain.dashboard.usecase

import ru.vt.core.common.UseCase
import ru.vt.domain.dashboard.entity.DashboardEntity
import ru.vt.domain.dashboard.repository.DashboardRepository

class GetDashboardUseCase(
    private val repository: DashboardRepository
) : UseCase<GetDashboardUseCase.Params, DashboardEntity>() {

    data class Params(val profileId: Long)

    override suspend fun execute(params: Params): Result<DashboardEntity> =
        Result.success(repository.getDashboard(params.profileId))
}
