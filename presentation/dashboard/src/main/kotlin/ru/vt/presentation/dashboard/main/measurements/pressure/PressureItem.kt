package ru.vt.presentation.dashboard.main.measurements.pressure

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import ru.vt.core.resources.TextResources
import ru.vt.core.ui.compose.collectWithLifecycle
import ru.vt.core.ui.compose.theme.AppTheme
import ru.vt.core.ui.compose.theme.Medium
import ru.vt.core.ui.compose.theme.Normal
import ru.vt.core.ui.compose.theme.bloodPressure
import ru.vt.core.ui.compose.utils.textResource
import ru.vt.domain.common.AppUnits
import ru.vt.domain.common.Measurement
import ru.vt.presentation.dashboard.main.measurements.common.CommonItem
import ru.vt.presentation.dashboard.main.measurements.common.TrackedTime

@Composable
internal fun PressureItem(
    onItemClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val vm = PressureViewModel(coroutineScope = coroutineScope)

    LaunchedEffect(key1 = vm, block = {
        vm.submitAction(PressureViewModel.Action.Init)
    })

    PressureItem(vm = vm, onItemClick = onItemClick)
}

@Composable
private fun PressureItem(
    vm: PressureViewModel,
    onItemClick: () -> Unit
) {
    val viewState by collectWithLifecycle(flow = vm.stateFlow, initial = null)
    PressureItem(state = derivedStateOf { viewState }.value, onItemClick = onItemClick)
}

@Composable
private fun PressureItem(
    state: PressureViewModel.State?,
    onItemClick: () -> Unit
) {
    val resources = LocalContext.current.resources
    CommonItem(
        onItemClick = onItemClick,
        icon = rememberVectorPainter(image = Icons.Filled.Favorite),
        iconTint = MaterialTheme.colors.bloodPressure,
        title = TextResources.getNameOfMeasurement(
            Measurement.PRESSURE_AND_HEART_RATE,
            resources
        ),
        titleColor = MaterialTheme.colors.bloodPressure,
        isStateEmpty = state == null
    ) {
        requireNotNull(state)
        TrackedTime(
            modifier = Modifier,
            time = state.time
        )
        Row(
            modifier = Modifier
                .padding(top = Medium)
                .height(IntrinsicSize.Max),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${state.systolic} / ${state.diastolic}",
                style = MaterialTheme.typography.h5.copy(color = MaterialTheme.colors.primary)
            )
            Text(
                modifier = Modifier.padding(start = Medium),
                text = textResource(key = textResource(key = AppUnits.BLOOD_PRESSURE.key)),
                style = MaterialTheme.typography.subtitle2.copy(color = MaterialTheme.colors.secondaryVariant)
            )
        }

        if (state.heartRate != null) {
            Row(
                modifier = Modifier
                    .padding(top = Medium)
                    .height(IntrinsicSize.Max),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${state.heartRate}",
                    style = MaterialTheme.typography.h6.copy(color = MaterialTheme.colors.primary)
                )
                Text(
                    modifier = Modifier.padding(start = Medium),
                    text = textResource(key = textResource(key = AppUnits.HEART_RATE.key)),
                    style = MaterialTheme.typography.subtitle2.copy(color = MaterialTheme.colors.secondaryVariant)
                )
            }
        }
    }
}

@Preview("Preview", showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.background)
                .padding(Normal)
        ) {
            PressureItem(state = null) {}
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Medium)
            )
            PressureItem(state = PressureViewModel.State(time = "12.02.1993", 140, 60, null)) {}
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Medium)
            )
            PressureItem(state = PressureViewModel.State(time = "12.02.1993", 120, 90, 70)) {}
        }
    }
}