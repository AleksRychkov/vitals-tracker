package ru.vt.presentation.dashboard.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import ru.vt.core.common.ResourceManger
import ru.vt.core.common.event.EventHandler
import ru.vt.core.common.extension.lazyNone
import ru.vt.core.dependency.androidDeps
import ru.vt.core.dependency.annotation.DependencyAccessor
import ru.vt.core.dependency.dataDeps
import ru.vt.core.navigation.contract.DashboardToHeadaches
import ru.vt.core.navigation.contract.DashboardToPressureAndHeartRate
import ru.vt.core.navigation.contract.DashboardToWeight
import ru.vt.core.navigation.contract.Navigator
import ru.vt.core.ui.viewmodel.BaseViewModel
import ru.vt.core.ui.viewmodel.ViewModelFactory
import ru.vt.domain.common.Measurement
import ru.vt.domain.dashboard.entity.DashboardEntity
import ru.vt.domain.dashboard.entity.TrackedEntity
import ru.vt.domain.dashboard.repository.DashboardRepository
import ru.vt.domain.dashboard.usecase.GetDashboardUseCase
import ru.vt.domain.dashboard.usecase.UpdateTrackedItemsUseCase
import ru.vt.domain.profile.entity.ProfileEntity
import ru.vt.domain.profile.repository.ProfileRepository
import ru.vt.domain.profile.usecase.GetProfileUseCase

@OptIn(DependencyAccessor::class)
internal class DashboardViewModel(
    savedStateHandle: SavedStateHandle,
    eventHandler: EventHandler?,
    resourceManger: ResourceManger,
    navigator: Navigator,
    private val dashboardRepository: DashboardRepository,
    private val profileRepository: ProfileRepository
) : BaseViewModel<StateDashboard, ActionDashboard>(
    savedStateHandle = savedStateHandle,
    eventHandler = eventHandler,
    resourceManger = resourceManger,
    navigator = navigator
) {
    override val initialState: StateDashboard = StateDashboard()

    private val getProfileUseCase: GetProfileUseCase by lazyNone {
        GetProfileUseCase(repository = profileRepository)
    }
    private val getDashboardUseCase: GetDashboardUseCase by lazyNone {
        GetDashboardUseCase(repository = dashboardRepository)
    }
    private val updateTrackedItemsUseCase: UpdateTrackedItemsUseCase by lazyNone {
        UpdateTrackedItemsUseCase(repository = dashboardRepository)
    }

    override suspend fun processAction(action: ActionDashboard) {
        when (action) {
            is ActionDashboard.Init -> processInit(action.profileId)
            is ActionDashboard.UpdateTrackedMeasurement ->
                updateTrackedMeasurements(action.type, action.isTracked)
            is ActionDashboard.NavigateToMeasurement -> navigateTo(action.type)
        }
    }

    private fun processInit(profileId: Long) {
        combine(
            getProfileUseCase(GetProfileUseCase.Params(profileId = profileId)),
            getDashboardUseCase(GetDashboardUseCase.Params(profileId = profileId))
        ) { p: Result<ProfileEntity>, d: Result<DashboardEntity> ->
            if (p.isSuccess && d.isSuccess) {
                Result.success(Pair(p, d))
            } else {
                // at least one of the results should have exception
                Result.failure(p.exceptionOrNull() ?: d.exceptionOrNull()!!)
            }
        }
            .handle { result ->
                val profile: ProfileEntity = requireNotNull(result.first.getOrNull())
                val dashboard: DashboardEntity = requireNotNull(result.second.getOrNull())
                render(
                    state.copy(
                        profileId = profile.id,
                        profileName = profile.name,
                        trackedItems = mapTrackedItem(dashboard.trackedItems),
                        hasTrackedItems = dashboard.trackedItems.any { it.isTracked }
                    )
                )
            }
            .launchIn(viewModelScope)
    }

    private fun updateTrackedMeasurements(@Measurement type: Int, isTracked: Boolean) {
        val profileId = state.profileId
        val tmp = state.trackedItems.map { s ->
            TrackedEntity(type = s.type, isTracked = if (s.type == type) isTracked else s.isTracked)
        }
        updateTrackedItemsUseCase(
            UpdateTrackedItemsUseCase.Params(profileId = profileId, trackedItems = tmp)
        )
            .handle { dashboard ->
                render(
                    state.copy(
                        trackedItems = mapTrackedItem(dashboard.trackedItems),
                        hasTrackedItems = dashboard.trackedItems.any { it.isTracked }
                    )
                )
            }
            .launchIn(viewModelScope)
    }

    private fun navigateTo(@Measurement type: Int) {
        when (type) {
            Measurement.PRESSURE_AND_HEART_RATE -> navigator.navigate(
                DashboardToPressureAndHeartRate(state.profileId)
            )
            Measurement.HEADACHE -> navigator.navigate(
                DashboardToHeadaches(state.profileId)
            )
            Measurement.WEIGHT -> navigator.navigate(
                DashboardToWeight(state.profileId)
            )
        }
    }

    class Factory : ViewModelFactory<DashboardViewModel> {
        override fun create(handle: SavedStateHandle): DashboardViewModel =
            DashboardViewModel(
                savedStateHandle = handle,
                eventHandler = androidDeps.eventHandler,
                resourceManger = androidDeps.resourceManger,
                navigator = androidDeps.navigator,
                dashboardRepository = dataDeps.dashboardRepository,
                profileRepository = dataDeps.profileRepository
            )
    }
}
