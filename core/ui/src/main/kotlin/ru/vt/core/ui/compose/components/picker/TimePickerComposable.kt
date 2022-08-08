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
import org.threeten.bp.LocalTime
import ru.vt.core.sysservice.VibratorService
import ru.vt.core.ui.compose.LocalVibratorService
import ru.vt.core.ui.compose.components.WheelComposable
import ru.vt.core.ui.compose.theme.AppTheme
import ru.vt.core.ui.compose.theme.Normal

@Composable
fun TimePickerComposable(
    modifier: Modifier,
    time: Pair<Int, Int>? = null, // hours, minutes
    localTime: LocalTime? = null,
    onTimeChanged: (Pair<Int, Int>) -> Unit
) {
    val _time = time?.let { LocalTime.of(it.first, it.second) } ?: localTime ?: LocalTime.now()

    val hours: List<Int> = (0..23).toList()
    val minutes: List<Int> = (0..59).toList()

    val hour = remember { mutableStateOf(_time.hour) }
    val minute = remember { mutableStateOf(_time.minute) }

    val onHourChanged: (Int) -> Unit = {
        hour.value = it
        onTimeChanged(it to minute.value)
    }
    val onMinuteChanged: (Int) -> Unit = {
        minute.value = it
        onTimeChanged(hour.value to it)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(Normal)
    ) {
        val itemHeight: Dp = 30.dp
        val wheelWidth: Dp = 80.dp
        Row(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .height(IntrinsicSize.Max)
                .align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically
        ) {
            WheelComposable(
                modifier = Modifier.width(wheelWidth),
                value = derivedStateOf { hour.value }.value,
                itemHeight = itemHeight,
                data = hours,
                onItemSelected = onHourChanged
            )
            Text(
                modifier = Modifier,
                text = ":",
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.secondaryVariant
            )
            WheelComposable(
                modifier = Modifier.width(wheelWidth),
                value = derivedStateOf { minute.value }.value,
                itemHeight = itemHeight,
                data = minutes,
                onItemSelected = onMinuteChanged
            )
        }
        PickerIndicator(boxScope = this, itemHeight = itemHeight)
    }
}

@Preview("Preview", showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    AppTheme {
        CompositionLocalProvider(
            LocalVibratorService provides object : VibratorService {
                override fun performTick() {

                }
            }
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                val time = remember { mutableStateOf(12 to 13) }
                TimePickerComposable(
                    modifier = Modifier.fillMaxWidth(),
                    time = time.value,
                    onTimeChanged = {
                        time.value = it
                    })
            }
        }
    }
}
