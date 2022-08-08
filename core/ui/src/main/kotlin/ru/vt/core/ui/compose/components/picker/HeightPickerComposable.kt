package ru.vt.core.ui.compose.components.picker

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

private const val DEFAULT_HEIGHT = 170

@Composable
fun HeightPickerComposable(
    modifier: Modifier,
    height: Int? = null,
    onHeightSelected: (Int) -> Unit
) {
    val heights: List<Int> = (1..300).toList()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(Normal)
    ) {
        val itemHeight: Dp = 30.dp
        val wheelWidth: Dp = 100.dp

        WheelComposable(
            modifier = Modifier
                .width(wheelWidth)
                .align(Alignment.Center),
            value = height ?: DEFAULT_HEIGHT,
            itemHeight = itemHeight,
            data = heights,
            onItemSelected = onHeightSelected
        )

        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(start = 128.dp),
            text = textResource(key = AppUnits.CM.key),
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.secondaryVariant
        )
        PickerIndicator(boxScope = this, itemHeight = itemHeight)
    }
}

@Preview("HeightPickerComposable Preview", showBackground = true, showSystemUi = true)
@Composable
private fun HeightPickerComposablePreview() {
    AppTheme {
        CompositionLocalProvider(
            LocalVibratorService provides object : VibratorService {
                override fun performTick() {

                }
            }
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                val height = remember { mutableStateOf(170) }
                HeightPickerComposable(
                    modifier = Modifier.fillMaxWidth(),
                    height = height.value,
                    onHeightSelected = {
                        height.value = it
                    })
            }
        }
    }
}