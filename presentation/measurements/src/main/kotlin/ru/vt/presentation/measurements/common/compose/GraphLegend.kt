package ru.vt.presentation.measurements.common.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.vt.core.ui.compose.utils.textResource
import ru.vt.core.ui.compose.theme.AppTheme
import ru.vt.core.ui.compose.theme.Normal
import ru.vt.core.ui.compose.theme.Small
import ru.vt.presentation.measurements.common.entity.GraphLegendDto
import ru.vt.presentation.measurements.common.entity.GraphLegendFactory
import ru.vt.presentation.measurements.common.entity.GraphLegendType

@Composable
internal fun GraphLegend(
    modifier: Modifier,
    graphLegendTypes: List<GraphLegendDto>,
    toggleActivation: (GraphLegendDto) -> Unit
) {
    val iconSize = 10.dp
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        graphLegendTypes.forEach { dto ->
            GraphLegendFactory.getForType(dto.type, MaterialTheme.colors.isLight)?.let { item ->
                GraphLegendItem(
                    modifier = Modifier.clickable {
                        toggleActivation(dto)
                    },
                    textId = item.textRes,
                    icon = painterResource(id = item.drawableRes),
                    tint = item.drawableColor,
                    iconSize = iconSize,
                    activated = dto.activated
                )
            }
        }
    }
}

@Composable
private fun GraphLegendItem(
    modifier: Modifier,
    @StringRes textId: Int,
    icon: Painter,
    tint: Color,
    iconSize: Dp,
    activated: Boolean,
    textStyle: TextStyle = MaterialTheme.typography.body2
) {
    Row(
        modifier = modifier.width(IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val text = textResource(id = textId)
        val alpha = if (activated) 1.0f else 0.25f
        Icon(
            modifier = Modifier.size(iconSize),
            painter = icon,
            contentDescription = text,
            tint = tint.copy(alpha = alpha)
        )
        Text(
            modifier = Modifier.padding(start = Small),
            text = text,
            style = textStyle.copy(color = MaterialTheme.colors.secondaryVariant.copy(alpha = alpha))
        )
    }
}

@Preview(name = "Preview", showSystemUi = true, showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            val list = remember {
                mutableStateOf(
                    listOf(
                        GraphLegendDto(GraphLegendType.SYSTOLIC, true),
                        GraphLegendDto(GraphLegendType.DIASTOLIC, true),
                        GraphLegendDto(GraphLegendType.HEART_RATE, false)
                    )
                )
            }
            GraphLegend(
                modifier = Modifier.padding(horizontal = Normal),
                list.value
            ) { dto ->
                list.value =
                    list.value.map { if (it.type == dto.type) it.copy(activated = dto.activated.not()) else it }
            }
        }
    }
}
