package ru.vt.presentation.dashboard.main.measurements.weight

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import ru.vt.core.resources.TextResources
import ru.vt.core.ui.compose.collectWithLifecycle
import ru.vt.core.ui.compose.theme.*
import ru.vt.core.ui.compose.utils.textResource
import ru.vt.domain.common.AppUnits
import ru.vt.domain.common.Measurement
import ru.vt.presentation.dashboard.R
import ru.vt.presentation.dashboard.main.measurements.common.CommonItem
import ru.vt.presentation.dashboard.main.measurements.common.TrackedTime

@Composable
internal fun WeightItem(
    onItemClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val vm = WeightViewModel(coroutineScope = coroutineScope)

    LaunchedEffect(key1 = vm, block = {
        vm.submitAction(WeightViewModel.Action.Init)
    })

    WeightItem(vm = vm, onItemClick = onItemClick)
}

@Composable
private fun WeightItem(
    vm: WeightViewModel,
    onItemClick: () -> Unit
) {
    val viewState by collectWithLifecycle(flow = vm.stateFlow, initial = null)
    WeightItem(state = derivedStateOf { viewState }.value, onItemClick = onItemClick)
}

@Composable
private fun WeightItem(
    state: WeightViewModel.State?,
    onItemClick: () -> Unit
) {
    val resources = LocalContext.current.resources
    CommonItem(
        onItemClick = onItemClick,
        icon = painterResource(id = R.drawable.ic_weight_filled),
        iconTint = MaterialTheme.colors.weight,
        title = TextResources.getNameOfMeasurement(Measurement.WEIGHT, resources),
        titleColor = MaterialTheme.colors.weight,
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
                text = (state.weight / 1000).toString(),
                style = MaterialTheme.typography.h5.copy(color = MaterialTheme.colors.primary)
            )
            Text(
                modifier = Modifier.padding(end = Small, start = Tinny),
                text = textResource(key = textResource(key = AppUnits.KG.key)),
                style = MaterialTheme.typography.subtitle2.copy(color = MaterialTheme.colors.secondaryVariant)
            )
            if (state.weight % 1000 != 0) {
                Text(
                    text = (state.weight % 1000).toString(),
                    style = MaterialTheme.typography.h5.copy(color = MaterialTheme.colors.primary)
                )
                Text(modifier = Modifier.padding(start = Tinny),
                    text = textResource(key = textResource(key = AppUnits.GRAMS.key)),
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
            WeightItem(state = null) {}
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Medium)
            )
            WeightItem(state = WeightViewModel.State(time = "12.02.1993", 75350)) {}
        }
    }
}
