package ru.vt.presentation.dashboard.main.measurements.pressure

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
import ru.vt.domain.measurement.entity.BloodPressureEntity
import ru.vt.domain.measurement.usecase.bloodpressure.LatestBloodPressureUseCase
import ru.vt.domain.profile.usecase.GetDefaultProfileUseCase
import ru.vt.presentation.dashboard.main.measurements.common.Utils.convertDate

@OptIn(DependencyAccessor::class)
internal class PressureViewModel(
    coroutineScope: CoroutineScope
) : LightViewModel<PressureViewModel.State, PressureViewModel.Action>(
    coroutineScope = coroutineScope,
    eventHandler = androidDeps.eventHandler,
    resourceManger = androidDeps.resourceManger
) {
    private val useCase: LatestBloodPressureUseCase by lazyNone {
        LatestBloodPressureUseCase(dataDeps.bloodPressureRepository)
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
                    LatestBloodPressureUseCase.Params(profileId)
                )
            }
            .flowOnDefault()
            .onEach { result ->
                result.getOrNull()?.let {
                    render(mapPressureItem(it))
                }
            }
            .launchIn(coroutineScope)
    }

    private fun mapPressureItem(entity: BloodPressureEntity): State =
        State(
            convertDate(entity.timestamp),
            entity.systolic,
            entity.diastolic,
            entity.heartRate
        )

    sealed class Action {
        object Init : Action()
    }

    @Parcelize
    data class State(
        val time: String,
        val systolic: Int,
        val diastolic: Int,
        val heartRate: Int? = null
    ) : Parcelable
}
