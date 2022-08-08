package ru.vt.presentation.measurements.pressureheartrate.summary

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
import ru.vt.core.ui.compose.utils.OnLoadMore
import ru.vt.core.ui.viewmodel.withFactory
import ru.vt.domain.measurement.MeasurementConstants
import ru.vt.presentation.measurements.common.compose.*
import ru.vt.presentation.measurements.common.entity.GraphLegendDto
import ru.vt.presentation.measurements.common.entity.GraphLegendType
import ru.vt.presentation.measurements.common.entity.GraphValuesRange
import ru.vt.presentation.measurements.pressureheartrate.summary.ui.EntityItem

private val graphValuesRange = GraphValuesRange(
    min = MeasurementConstants.MIN_MAX_PHR.first,
    max = MeasurementConstants.MIN_MAX_PHR.second,
    steps = 4
)

@Composable
fun ScreenBloodPressure(profileId: Long) {
    val vm: ViewModelPHR = viewModel(factory = withFactory(factory = ViewModelPHR.Factory()))

    LaunchedEffect(key1 = vm, block = {
        if (vm.hasRestoredState().not() ||
            vm.stateFlow.firstOrNull() == null ||
            vm.stateFlow.firstOrNull()?.graphData == null
        ) {
            vm.submitAction(ActionPHR.Init(profileId = profileId))
        }
    })

    ScreenPHR(vm = vm, initialState = vm.initialState())
}

@Composable
private fun ScreenPHR(vm: ViewModelPHR, initialState: StatePHR) {
    val viewState by collectWithLifecycle(flow = vm.stateFlow, initial = initialState)

    ScreenPHR(state = derivedStateOf { viewState }.value, actionHandler = {
        vm.submitAction(it)
    })
}

@Composable
private fun ScreenPHR(
    state: StatePHR,
    actionHandler: (ActionPHR) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val floatingBtnAdd = remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }

    if (state.data?.isNotEmpty() == true && state.data.size > 5 && state.canLoadMore) {
        listState.OnLoadMore(buffer = 5) {
            actionHandler(ActionPHR.LoadMore)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        MeasurementAppBar(
            title = R.string.measurement_blood_pressure_and_heart_rate,
            onAdd = { actionHandler(ActionPHR.Create) },
            onGenerateReport = {},
            onBackPressed = { actionHandler(ActionPHR.OnBackPressed) },
        )
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()

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
                            actionHandler(ActionPHR.SetPeriod(it))
                        }
                    }
                }

                item(key = "key.graph_section") {
                    val legendTypes = rememberSaveable {
                        mutableStateOf(
                            listOf(
                                GraphLegendDto(GraphLegendType.SYSTOLIC, true),
                                GraphLegendDto(GraphLegendType.DIASTOLIC, true),
                                GraphLegendDto(GraphLegendType.HEART_RATE, false),
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
                                actionHandler(ActionPHR.DeleteEntity(entity = entity))
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
                actionHandler(ActionPHR.Create)
            }
        }

        LaunchedEffect(key1 = state.period, block = {
            listState.scrollToItem(0)
        })
    }
}
