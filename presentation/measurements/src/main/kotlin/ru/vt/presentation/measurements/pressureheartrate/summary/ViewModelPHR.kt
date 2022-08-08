package ru.vt.presentation.measurements.pressureheartrate.summary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import org.threeten.bp.LocalDate
import ru.vt.core.common.LoaderHandler
import ru.vt.core.common.ResourceManger
import ru.vt.core.common.event.EventHandler
import ru.vt.core.common.extension.flowOnDefault
import ru.vt.core.common.extension.lazyNone
import ru.vt.core.dependency.androidDeps
import ru.vt.core.dependency.annotation.DependencyAccessor
import ru.vt.core.dependency.dataDeps
import ru.vt.core.navigation.contract.Navigator
import ru.vt.core.navigation.contract.ScreenPHRToCreate
import ru.vt.core.ui.viewmodel.BaseViewModel
import ru.vt.core.ui.viewmodel.ViewModelFactory
import ru.vt.domain.measurement.entity.BloodPressureEntity
import ru.vt.domain.measurement.repository.BloodPressureRepository
import ru.vt.domain.measurement.usecase.bloodpressure.DeleteBloodPressureUseCase
import ru.vt.domain.measurement.usecase.bloodpressure.GetBloodPressureMinMaxUseCase
import ru.vt.domain.measurement.usecase.bloodpressure.GetBloodPressureUseCase
import ru.vt.presentation.measurements.common.entity.*
import ru.vt.presentation.measurements.pressureheartrate.BloodPressureGraphMapper

@OptIn(DependencyAccessor::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
internal class ViewModelPHR(
    savedStateHandle: SavedStateHandle,
    eventHandler: EventHandler?,
    resourceManger: ResourceManger,
    navigator: Navigator,
    private val repository: BloodPressureRepository,
    private val loaderHandler: LoaderHandler?
) : BaseViewModel<StatePHR, ActionPHR>(
    savedStateHandle = savedStateHandle,
    eventHandler = eventHandler,
    resourceManger = resourceManger,
    navigator = navigator
) {
    private companion object {
        const val LIMIT: Int = 20
    }

    private val getUseCase: GetBloodPressureUseCase by lazyNone {
        GetBloodPressureUseCase(repository)
    }
    private val deleteUseCase: DeleteBloodPressureUseCase by lazyNone {
        DeleteBloodPressureUseCase(repository)
    }
    private val minMaxUseCase: GetBloodPressureMinMaxUseCase by lazyNone {
        GetBloodPressureMinMaxUseCase(repository)
    }

    private val loadDataFlow: MutableSharedFlow<Pair<Period, Int>> = MutableSharedFlow()
    private val loadGraphFlow: MutableSharedFlow<Period> = MutableSharedFlow()

    override val initialState: StatePHR by lazyNone {
        StatePHR(anchorDate = LocalDate.now().toAnchorDate())
    }

    override suspend fun processAction(action: ActionPHR) {
        when (action) {
            is ActionPHR.Init -> {
                render(state.copy(profileId = action.profileId, data = null, graphData = null))
                loadDataFlow.emit(state.period to 0)
                loadGraphFlow.emit(state.period)
            }
            is ActionPHR.SetPeriod -> {
                render(state.copy(period = action.period, data = null, graphData = null))
                loadDataFlow.emit(action.period to 0)
                loadGraphFlow.emit(action.period)
            }
            ActionPHR.OnBackPressed -> navigator.onBackPressed()
            is ActionPHR.DeleteEntity -> deleteEntity(action.entity)
            ActionPHR.Create -> navigator.navigate(ScreenPHRToCreate(state.profileId))
            ActionPHR.LoadMore -> loadDataFlow.emit(state.period to (state.data?.size ?: 0))
        }
    }

    init {
        loadDataFlow
            .onEach { loaderHandler?.setLoading(true) }
            .flatMapLatest { p ->
                val anchorLocalDate = LocalDate.of(
                    state.anchorDate.year,
                    state.anchorDate.month,
                    state.anchorDate.day
                )
                val (from, to) = p.first.getFromToValues(anchorLocalDate)
                getUseCase(
                    GetBloodPressureUseCase.Params(
                        profileId = state.profileId,
                        from = from,
                        to = to,
                        offset = p.second,
                        limit = LIMIT
                    )
                )
            }
            .flowOnDefault()
            .onEach { loaderHandler?.setLoading(false) }
            .handle { data ->
                val newData = state.data?.toMutableList() ?: mutableListOf()
                newData.addAll(data)
                render(state.copy(data = newData, canLoadMore = data.size == LIMIT))
            }
            .launchIn(viewModelScope)
        loadGraphFlow
            .onEach { loaderHandler?.setLoading(true) }
            .flatMapLatest { period ->
                val anchorLocalDate = LocalDate.of(
                    state.anchorDate.year,
                    state.anchorDate.month,
                    state.anchorDate.day
                )
                val bounds = period.getBounds(anchorLocalDate)
                minMaxUseCase(
                    GetBloodPressureMinMaxUseCase.Params(
                        profileId = state.profileId,
                        bounds = bounds
                    )
                ).map { result ->
                    if (result.isSuccess) {
                        Result.success(BloodPressureGraphMapper.map(result.getOrDefault(emptyList())))
                    } else {
                        Result.failure(result.exceptionOrNull() ?: IllegalStateException())
                    }
                }.catch { exception -> Result.failure<List<GraphSection>>(exception) }
            }
            .flowOnDefault()
            .handle { graphSections ->
                render(state.copy(graphData = graphSections))
                loaderHandler?.setLoading(false)
            }
            .launchIn(viewModelScope)
    }

    private fun deleteEntity(entity: BloodPressureEntity) {
        deleteUseCase(DeleteBloodPressureUseCase.Params(entity.profileId, entity.timestamp))
            .handle { success ->
                if (success) {
                    loadGraphFlow.emit(state.period)
                    render(state.copy(data = state.data?.filter { it != entity }))
                }
            }
            .launchIn(viewModelScope)
    }

    class Factory : ViewModelFactory<ViewModelPHR> {
        override fun create(handle: SavedStateHandle): ViewModelPHR = ViewModelPHR(
            savedStateHandle = handle,
            eventHandler = androidDeps.eventHandler,
            resourceManger = androidDeps.resourceManger,
            navigator = androidDeps.navigator,
            repository = dataDeps.bloodPressureRepository,
            loaderHandler = androidDeps.loaderHandler
        )
    }
}
