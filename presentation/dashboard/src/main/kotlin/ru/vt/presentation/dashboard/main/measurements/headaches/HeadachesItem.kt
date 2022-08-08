package ru.vt.presentation.dashboard.main.measurements.headaches

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import ru.vt.core.resources.R
import ru.vt.core.resources.TextResources
import ru.vt.core.ui.compose.collectWithLifecycle
import ru.vt.core.ui.compose.theme.Medium
import ru.vt.core.ui.compose.theme.headaches
import ru.vt.core.ui.compose.utils.textResource
import ru.vt.domain.common.Measurement
import ru.vt.domain.common.MeasurementParams
import ru.vt.domain.measurement.entity.HeadacheArea
import ru.vt.presentation.dashboard.main.measurements.common.CommonItem
import ru.vt.presentation.dashboard.main.measurements.common.TrackedTime

@Composable
internal fun HeadachesItem(
    onItemClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val vm = HeadachesViewModel(coroutineScope = coroutineScope)
    LaunchedEffect(key1 = vm, block = {
        vm.submitAction(HeadachesViewModel.Action.Init)
    })

    HeadachesItem(vm = vm, onItemClick = onItemClick)
}

@Composable
private fun HeadachesItem(
    vm: HeadachesViewModel,
    onItemClick: () -> Unit
) {
    val viewState by collectWithLifecycle(flow = vm.stateFlow, initial = null)

    HeadachesItem(state = derivedStateOf { viewState }.value, onItemClick = onItemClick)
}

@Composable
private fun HeadachesItem(
    state: HeadachesViewModel.State?,
    onItemClick: () -> Unit
) {
    val resources = LocalContext.current.resources
    CommonItem(
        onItemClick = onItemClick,
        icon = rememberVectorPainter(image = Icons.Filled.Bolt),
        iconTint = MaterialTheme.colors.headaches,
        title = TextResources.getNameOfMeasurement(
            Measurement.HEADACHE,
            resources
        ),
        titleColor = MaterialTheme.colors.headaches,
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
                text = "${state.intensity}",
                style = MaterialTheme.typography.h5.copy(color = MaterialTheme.colors.primary)
            )
            Text(
                modifier = Modifier.padding(start = Medium),
                text = textResource(key = textResource(key = MeasurementParams.HEADACHE_INTENSITY.key)).lowercase(),
                style = MaterialTheme.typography.subtitle2.copy(color = MaterialTheme.colors.secondaryVariant)
            )
        }

        if (state.area != HeadacheArea.UNDEFINED) {
            Row(
                modifier = Modifier
                    .padding(top = Medium)
                    .height(IntrinsicSize.Max),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = textResource(key = state.area.key),
                    style = MaterialTheme.typography.h6.copy(color = MaterialTheme.colors.primary)
                )
                Text(
                    modifier = Modifier.padding(start = Medium),
                    text = textResource(id = R.string.screen_create_headaches_area).lowercase(),
                    style = MaterialTheme.typography.subtitle2.copy(color = MaterialTheme.colors.secondaryVariant)
                )
            }
        }
    }
}
