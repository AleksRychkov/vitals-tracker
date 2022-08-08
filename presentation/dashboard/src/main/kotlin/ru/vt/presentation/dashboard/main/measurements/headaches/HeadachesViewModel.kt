package ru.vt.presentation.dashboard.main.measurements.headaches

import android.os.Parcelable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.parcelize.Parcelize
import ru.vt.core.common.extension.flowOnDefault
import ru.vt.core.common.extension.lazyNone
import ru.vt.core.dependency.androidDeps
import ru.vt.core.dependency.annotation.DependencyAccessor
import ru.vt.core.dependency.dataDeps
import ru.vt.core.ui.viewmodel.LightViewModel
import ru.vt.domain.measurement.entity.HeadacheArea
import ru.vt.domain.measurement.entity.HeadacheEntity
import ru.vt.domain.measurement.usecase.headache.LatestHeadacheUseCase
import ru.vt.domain.profile.usecase.GetDefaultProfileUseCase
import ru.vt.presentation.dashboard.main.measurements.common.Utils.convertDate

@OptIn(DependencyAccessor::class)
internal class HeadachesViewModel(
    coroutineScope: CoroutineScope
) : LightViewModel<HeadachesViewModel.State, HeadachesViewModel.Action>(
    coroutineScope = coroutineScope,
    eventHandler = androidDeps.eventHandler,
    resourceManger = androidDeps.resourceManger
) {

    private val profileUseCase: GetDefaultProfileUseCase by lazyNone {
        GetDefaultProfileUseCase(dataDeps.profileRepository)
    }
    private val useCase: LatestHeadacheUseCase by lazyNone {
        LatestHeadacheUseCase(dataDeps.headacheRepository)
    }

    override suspend fun processAction(action: Action) {
        if (action == Action.Init) {
            loadInitial()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadInitial() {
        profileUseCase(Unit)
            .flatMapLatest {
                val profileId = it.getOrNull()!!.id
                useCase(
                    LatestHeadacheUseCase.Params(profileId)
                )
            }
            .flowOnDefault()
            .onEach { result ->
                result.getOrNull()?.let { entity ->
                    render(mapHeadacheItem(entity))
                }
            }
            .launchIn(coroutineScope)
    }

    private fun mapHeadacheItem(entity: HeadacheEntity): State =
        State(
            time = convertDate(entity.timestamp),
            intensity = entity.intensity,
            area = entity.headacheArea
        )

    sealed class Action {
        object Init : Action()
    }

    @Parcelize
    data class State(
        val time: String,
        val intensity: Int,
        val area: HeadacheArea
    ) : Parcelable
}
