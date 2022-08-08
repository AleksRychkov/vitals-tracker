package ru.vt.presentation.dashboard.main.measurements.weight

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
import ru.vt.domain.measurement.entity.WeightEntity
import ru.vt.domain.measurement.usecase.weight.LatestWeightUseCase
import ru.vt.domain.profile.usecase.GetDefaultProfileUseCase
import ru.vt.presentation.dashboard.main.measurements.common.Utils.convertDate

@OptIn(DependencyAccessor::class)
internal class WeightViewModel(
    coroutineScope: CoroutineScope
) : LightViewModel<WeightViewModel.State, WeightViewModel.Action>(
    coroutineScope = coroutineScope,
    eventHandler = androidDeps.eventHandler,
    resourceManger = androidDeps.resourceManger
) {

    private val useCase: LatestWeightUseCase by lazyNone {
        LatestWeightUseCase(dataDeps.weightRepository)
    }
    private val profileUseCase: GetDefaultProfileUseCase by lazyNone {
        GetDefaultProfileUseCase(dataDeps.profileRepository)
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
                    LatestWeightUseCase.Params(profileId)
                )
            }
            .flowOnDefault()
            .onEach { result ->
                result.getOrNull()?.let {
                    render(mapWeightItem(it))
                }
            }
            .launchIn(coroutineScope)
    }

    private fun mapWeightItem(entity: WeightEntity): State =
        State(
            time = convertDate(entity.timestamp),
            weight = entity.weightInGrams
        )

    sealed class Action {
        object Init : Action()
    }

    @Parcelize
    data class State(
        val time: String,
        val weight: Int
    ) : Parcelable
}
