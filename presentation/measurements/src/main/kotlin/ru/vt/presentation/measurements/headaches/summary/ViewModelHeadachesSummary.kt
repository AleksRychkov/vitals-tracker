package ru.vt.presentation.measurements.headaches.summary

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
import ru.vt.core.navigation.contract.ScreenHeadachesToCreate
import ru.vt.core.ui.viewmodel.BaseViewModel
import ru.vt.core.ui.viewmodel.ViewModelFactory
import ru.vt.domain.measurement.entity.HeadacheEntity
import ru.vt.domain.measurement.repository.HeadacheRepository
import ru.vt.domain.measurement.usecase.headache.DeleteHeadacheUseCase
import ru.vt.domain.measurement.usecase.headache.GetHeadacheMinMaxUseCase
import ru.vt.domain.measurement.usecase.headache.GetHeadacheUseCase
import ru.vt.presentation.measurements.common.entity.*
import ru.vt.presentation.measurements.headaches.HeadachesGraphMapper
import ru.vt.presentation.measurements.headaches.summary.ActionHeadachesSummary as Action
import ru.vt.presentation.measurements.headaches.summary.StateHeadachesSummary as State

@OptIn(DependencyAccessor::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
internal class ViewModelHeadachesSummary(
    savedStateHandle: SavedStateHandle,
    eventHandler: EventHandler?,
    resourceManger: ResourceManger,
    navigator: Navigator,
    private val repository: HeadacheRepository,
    private val loaderHandler: LoaderHandler?
) : BaseViewModel<State, Action>(
    savedStateHandle = savedStateHandle,
    eventHandler = eventHandler,
    resourceManger = resourceManger,
    navigator = navigator
) {

    private companion object {
        const val LIMIT: Int = 20
    }

    private val getUseCase: GetHeadacheUseCase by lazyNone {
        GetHeadacheUseCase(repository)
    }

    private val deleteUseCase: DeleteHeadacheUseCase by lazyNone {
        DeleteHeadacheUseCase(repository)
    }

    private val minMaxUseCase: GetHeadacheMinMaxUseCase by lazyNone {
        GetHeadacheMinMaxUseCase(repository)
    }

    private val loadDataFlow: MutableSharedFlow<Pair<Period, Int>> = MutableSharedFlow()
    private val loadGraphFlow: MutableSharedFlow<Period> = MutableSharedFlow()

    override val initialState: State by lazyNone {
        State(anchorDate = LocalDate.now().toAnchorDate())
    }

    override suspend fun processAction(action: Action) {
        when (action) {
            is Action.Init -> {
                render(state.copy(profileId = action.profileId, data = null, graphData = null))
                loadDataFlow.emit(state.period to 0)
                loadGraphFlow.emit(state.period)
            }
            is Action.SetPeriod -> {
                render(state.copy(period = action.period, data = null, graphData = null))
                loadDataFlow.emit(action.period to 0)
                loadGraphFlow.emit(action.period)
            }
            is Action.Delete -> deleteEntity(action.entity)
            Action.OnBackPressed -> navigator.onBackPressed()
            Action.Create -> navigator.navigate(ScreenHeadachesToCreate(state.profileId))
            Action.LoadMore -> loadDataFlow.emit(state.period to (state.data?.size ?: 0))
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
                    GetHeadacheUseCase.Params(
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
                    GetHeadacheMinMaxUseCase.Params(
                        profileId = state.profileId,
                        bounds = bounds
                    )
                ).map { result ->
                    if (result.isSuccess) {
                        Result.success(HeadachesGraphMapper.map(result.getOrDefault(emptyList())))
                    } else {
                        Result.failure(result.exceptionOrNull() ?: IllegalStateException())
                    }
                }.catch { exception -> Result.failure<List<GraphSection>>(exception) }
            }
            .flowOnDefault()
            .onEach { loaderHandler?.setLoading(false) }
            .handle { graphSections ->
                render(state.copy(graphData = graphSections))
            }
            .launchIn(viewModelScope)
    }

    private fun deleteEntity(entity: HeadacheEntity) {
        deleteUseCase(DeleteHeadacheUseCase.Params(entity.profileId, entity.timestamp))
            .handle {
                loadGraphFlow.emit(state.period)
                render(state.copy(data = state.data?.filter { it != entity }))
            }
            .launchIn(viewModelScope)
    }

    class Factory : ViewModelFactory<ViewModelHeadachesSummary> {
        override fun create(handle: SavedStateHandle): ViewModelHeadachesSummary =
            ViewModelHeadachesSummary(
                savedStateHandle = handle,
                eventHandler = androidDeps.eventHandler,
                resourceManger = androidDeps.resourceManger,
                navigator = androidDeps.navigator,
                repository = dataDeps.headacheRepository,
                loaderHandler = androidDeps.loaderHandler
            )
    }
}
