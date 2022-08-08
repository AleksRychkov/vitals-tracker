package ru.vt.presentation.dashboard.main.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.navigationBarsWithImePadding
import ru.vt.core.ui.compose.theme.Normal
import ru.vt.domain.common.Measurement
import ru.vt.presentation.dashboard.main.ActionDashboard
import ru.vt.presentation.dashboard.main.StateDashboard
import ru.vt.presentation.dashboard.main.measurements.headaches.HeadachesItem
import ru.vt.presentation.dashboard.main.measurements.pressure.PressureItem
import ru.vt.presentation.dashboard.main.measurements.weight.WeightItem

@Composable
internal fun DashboardMeasurements(
    modifier: Modifier = Modifier,
    state: StateDashboard,
    actionHandler: (ActionDashboard) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Normal),
        verticalArrangement = Arrangement.spacedBy(Normal)
    ) {
        val trackedItems = derivedStateOf { state.trackedItems.filter { it.isTracked } }.value
        trackedItems.forEach {
            when (it.type) {
                Measurement.PRESSURE_AND_HEART_RATE -> PressureItem {
                    actionHandler(ActionDashboard.NavigateToMeasurement(Measurement.PRESSURE_AND_HEART_RATE))
                }
                Measurement.HEADACHE -> HeadachesItem {
                    actionHandler(ActionDashboard.NavigateToMeasurement(Measurement.HEADACHE))
                }
                Measurement.WEIGHT -> WeightItem {
                    actionHandler(ActionDashboard.NavigateToMeasurement(Measurement.WEIGHT))
                }
            }
        }
    }
}