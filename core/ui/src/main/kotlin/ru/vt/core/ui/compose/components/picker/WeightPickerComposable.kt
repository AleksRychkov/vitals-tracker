package ru.vt.core.ui.compose.components.picker

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.vt.core.sysservice.VibratorService
import ru.vt.core.ui.compose.LocalVibratorService
import ru.vt.core.ui.compose.components.WheelComposable
import ru.vt.core.ui.compose.utils.textResource
import ru.vt.core.ui.compose.theme.AppTheme
import ru.vt.core.ui.compose.theme.Normal
import ru.vt.domain.common.AppUnits

private const val KG_UNIT = 1000
private const val DEFAULT_WEIGHT = 70 * 1000

@Composable
fun WeightPickerComposable(
    modifier: Modifier,
    weight: Int? = null,
    onWeightChanged: (Int) -> Unit
) {
    val kilograms: List<Int> = (1..300).toList()
    val grams: List<Int> = (0..9).map { it * 100 }.toList()

    val kg = remember { mutableStateOf((weight ?: DEFAULT_WEIGHT) / KG_UNIT) }
    val gram =
        remember { mutableStateOf(((((weight ?: DEFAULT_WEIGHT) % KG_UNIT) + 99) / 100) * 100) }

    val onKgChanged: (Int) -> Unit = {
        kg.value = it
        onWeightChanged(it * KG_UNIT + gram.value)
    }
    val onGChanged: (Int) -> Unit = {
        gram.value = it
        onWeightChanged(kg.value * KG_UNIT + gram.value)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(Normal)
    ) {
        val itemHeight: Dp = 30.dp
        val wheelWidth: Dp = 100.dp
        Row(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .height(IntrinsicSize.Max)
                .align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically
        ) {
            WheelComposable(
                modifier = Modifier.width(wheelWidth),
                value = derivedStateOf { kg.value }.value,
                itemHeight = itemHeight,
                data = kilograms,
                onItemSelected = onKgChanged
            )

            Text(
                modifier = Modifier,
                text = textResource(key = AppUnits.KG.key),
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.secondaryVariant
            )

            WheelComposable(
                modifier = Modifier.width(wheelWidth),
                value = derivedStateOf { gram.value }.value,
                itemHeight = itemHeight,
                data = grams,
                onItemSelected = onGChanged
            )

            Text(
                modifier = Modifier,
                text = textResource(key = AppUnits.GRAMS.key),
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.secondaryVariant
            )
        }

        PickerIndicator(boxScope = this, itemHeight = itemHeight)
    }
}

@Preview("WeightPickerComposable Preview", showBackground = true, showSystemUi = true)
@Composable
private fun WeightPickerComposablePreview() {
    AppTheme {
        CompositionLocalProvider(
            LocalVibratorService provides object : VibratorService {
                override fun performTick() {

                }
            }
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                val weight = remember { mutableStateOf(80 * 1000) }
                WeightPickerComposable(
                    modifier = Modifier.fillMaxWidth(),
                    weight = weight.value,
                    onWeightChanged = {
                        weight.value = it
                    })
            }
        }
    }
}