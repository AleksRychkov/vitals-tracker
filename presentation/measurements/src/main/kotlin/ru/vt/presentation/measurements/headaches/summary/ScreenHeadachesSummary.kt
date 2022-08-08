package ru.vt.presentation.measurements.headaches.summary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.firstOrNull
import ru.vt.core.resources.R
import ru.vt.core.ui.compose.collectWithLifecycle
import ru.vt.core.ui.compose.components.BottomShadowComposable
import ru.vt.core.ui.compose.components.visibility.FadeInOutAnimatedVisibility
import ru.vt.core.ui.compose.theme.Normal
import ru.vt.core.ui.compose.theme.measurableBG
import ru.vt.core.ui.viewmodel.withFactory
import ru.vt.domain.measurement.MeasurementConstants.HEADACHE_INTENSITY_MAX
import ru.vt.domain.measurement.MeasurementConstants.HEADACHE_INTENSITY_MIN
import ru.vt.presentation.measurements.common.compose.*
import ru.vt.presentation.measurements.common.entity.GraphLegendDto
import ru.vt.presentation.measurements.common.entity.GraphLegendType
import ru.vt.presentation.measurements.common.entity.GraphValuesRange
import ru.vt.presentation.measurements.headaches.summary.ui.EntityItem
import ru.vt.presentation.measurements.headaches.summary.ActionHeadachesSummary as Action
import ru.vt.presentation.measurements.headaches.summary.StateHeadachesSummary as State

private val graphValuesRange = GraphValuesRange(
    min = HEADACHE_INTENSITY_MIN,
    max = HEADACHE_INTENSITY_MAX,
    steps = 3
)

@Composable
fun ScreenHeadaches(profileId: Long) {
    val vm: ViewModelHeadachesSummary =
        viewModel(factory = withFactory(factory = ViewModelHeadachesSummary.Factory()))

    LaunchedEffect(key1 = vm, block = {
        if (vm.hasRestoredState().not() ||
            vm.stateFlow.firstOrNull() == null
        ) {
            vm.submitAction(Action.Init(profileId = profileId))
        }
    })
    Summary(vm = vm, initialState = vm.initialState())
}

@Composable
private fun Summary(vm: ViewModelHeadachesSummary, initialState: State) {
    val viewState by collectWithLifecycle(flow = vm.stateFlow, initial = initialState)

    Summary(state = viewState, actionHandler = {
        vm.submitAction(it)
    })
}

@Composable
private fun Summary(state: State, actionHandler: (Action) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val floatingBtnAdd = remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        MeasurementAppBar(
            title = R.string.measurement_headache,
            onAdd = { actionHandler(Action.Create) },
            onGenerateReport = {},
            onBackPressed = { actionHandler(Action.OnBackPressed) },
        )
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxWidth()
            ) {
                item(key = "key.graph_period_tab") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Max)
                            .background(color = MaterialTheme.colors.measurableBG)
                    ) {
                        PeriodTabBar(
                            modifier = Modifier.padding(Normal),
                            selected = state.period
                        ) {
                            actionHandler(Action.SetPeriod(it))
                        }
                    }
                }

                item(key = "key.graph_section") {
                    val legendTypes = rememberSaveable {
                        mutableStateOf(
                            listOf(
                                GraphLegendDto(GraphLegendType.HEADACHE_INTENSITY, true)
                            )
                        )
                    }
                    GraphLegend(
                        modifier = Modifier
                            .background(color = MaterialTheme.colors.measurableBG)
                            .padding(horizontal = Normal)
                            .padding(bottom = Normal),
                        graphLegendTypes = legendTypes.value
                    ) { dto ->
                        legendTypes.value =
                            legendTypes.value.map { if (it.type == dto.type) it.copy(activated = dto.activated.not()) else it }
                    }

                    Graph(
                        modifier = Modifier.background(color = MaterialTheme.colors.measurableBG),
                        period = state.period,
                        anchorDate = state.anchorDate,
                        graphValuesRange = graphValuesRange,
                        graphData = state.graphData ?: emptyList(),
                        graphLegendTypes = legendTypes.value
                    )
                }

                state.data?.forEachIndexed { index, entity ->
                    item(key = "key.${entity.timestamp}") {
                        EntityItem(
                            modifier = Modifier,
                            index = index,
                            entity = entity,
                            deleteEntity = { entity ->
                                actionHandler(Action.Delete(entity))
                            }
                        )
                    }
                }
                if (state.data != null && state.data.isNotEmpty()) {
                    item(key = "key.floating_btn_padding") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                        )
                    }
                }
                if (state.data == null || state.data.isEmpty()) {
                    item(key = "key.no_data") {
                        NoDataItem(modifier = Modifier)
                    }
                }
            }

            FadeInOutAnimatedVisibility(isVisible = floatingBtnAdd.value.not()) {
                BottomShadowComposable(modifier = Modifier.fillMaxWidth())
            }

            MeasurementFloatingButton(
                boxScope = this,
                coroutineScope = coroutineScope,
                listState = listState,
                floatingBtnAdd = floatingBtnAdd
            ) {
                actionHandler(Action.Create)
            }
        }

        LaunchedEffect(key1 = state.period, block = {
            listState.scrollToItem(0)
        })
    }
}
