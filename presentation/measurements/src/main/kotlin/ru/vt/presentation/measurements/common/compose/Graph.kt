package ru.vt.presentation.measurements.common.compose

import android.graphics.Rect
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.threeten.bp.DayOfWeek
import org.threeten.bp.Month
import org.threeten.bp.format.TextStyle
import ru.vt.core.ui.compose.theme.*
import ru.vt.core.ui.compose.utils.dpToPx
import ru.vt.core.ui.compose.utils.spToPx
import ru.vt.presentation.measurements.common.entity.*
import java.util.*


@Composable
internal fun Graph(
    modifier: Modifier,
    period: Period,
    anchorDate: AnchorDate,
    graphValuesRange: GraphValuesRange,
    graphData: List<GraphSection> = emptyList(),
    graphLegendTypes: List<GraphLegendDto>
) {
    val secondaryColor = MaterialTheme.colors.divider
    val textPaint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        textSize = spToPx(sp = 14.sp)
        color = secondaryColor.toArgb()
    }
    val textBounds = Rect()
    textPaint.getTextBounds("999", 0, 3, textBounds)

    val utilData = UtilData(
        offset = Offset(x = dpToPx(dp = Normal), y = dpToPx(dp = Medium)),
        textBounds = textBounds,
        lineColor = secondaryColor,
        strokeWidth = dpToPx(dp = 1.dp),
        textPaint = textPaint
    )

    val legendTypesMap: MutableMap<Int, Pair<Painter, Color>> = mutableMapOf()
    graphLegendTypes.filter { it.activated }.map { it.type }.forEach { type ->
        GraphLegendFactory.getForType(type = type, isLight = MaterialTheme.colors.isLight)?.let {
            legendTypesMap[type] = painterResource(id = it.drawableRes) to it.drawableColor
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.25f),
        onDraw = {
            val virtualWidth = size.width - utilData.offset.x * 2f - textBounds.width()
            val virtualHeight = size.height - utilData.offset.y * 2f - textBounds.height() * 2f

            borderLines(
                drawScope = this,
                utilData = utilData
            )
            gridLines(
                drawScope = this,
                period = period,
                graphValuesRange = graphValuesRange,
                utilData = utilData,
                virtualWidth = virtualWidth,
                virtualHeight = virtualHeight
            )
            gridTexts(
                drawScope = this,
                period = period,
                graphValuesRange = graphValuesRange,
                utilData = utilData,
                virtualWidth = virtualWidth,
                virtualHeight = virtualHeight,
                daysInMonth = anchorDate.daysInMonth,
                currentMonth = anchorDate.month
            )
            data(
                drawScope = this,
                utilData = utilData,
                virtualWidth = virtualWidth,
                virtualHeight = virtualHeight,
                data = graphData,
                graphValuesRange = graphValuesRange,
                legends = legendTypesMap
            )
        }
    )
}

private fun borderLines(
    drawScope: DrawScope,
    utilData: UtilData
) {
    val offset: Offset = utilData.offset
    val textBounds: Rect = utilData.textBounds
    val lineColor: Color = utilData.lineColor
    val strokeWidth: Float = utilData.strokeWidth
    with(drawScope) {
        // left vertical line
        drawLine(
            color = lineColor,
            start = offset,
            end = Offset(offset.x, size.height - offset.y),
            strokeWidth = strokeWidth
        )
        // right vertical line
        drawLine(
            color = lineColor,
            start = Offset(size.width - offset.x - textBounds.width(), offset.y),
            end = Offset(size.width - offset.x - textBounds.width(), size.height - offset.y),
            strokeWidth = strokeWidth
        )
        // top horizontal line
        drawLine(
            color = lineColor,
            start = offset,
            end = Offset(size.width - offset.x - textBounds.width(), offset.y),
            strokeWidth = strokeWidth
        )
        // bottom horizontal line
        drawLine(
            color = lineColor,
            start = Offset(offset.x, size.height - offset.y - textBounds.height() * 2f),
            end = Offset(
                size.width - offset.x - textBounds.width(),
                size.height - offset.y - textBounds.height() * 2f
            ),
            strokeWidth = strokeWidth
        )
    }
}

private fun gridLines(
    drawScope: DrawScope,
    period: Period,
    graphValuesRange: GraphValuesRange,
    utilData: UtilData,
    virtualWidth: Float,
    virtualHeight: Float
) {
    val offset: Offset = utilData.offset
    val textBounds: Rect = utilData.textBounds
    val lineColor: Color = utilData.lineColor
    val strokeWidth: Float = utilData.strokeWidth
    with(drawScope) {
        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(13f, 7f), 0F)

        // vertical lines
        val (stepWidth, verticalLines) = when (period) {
            Period.Day -> virtualWidth / 4f to 3
            Period.Week -> virtualWidth / 7f to 6
            Period.Month -> virtualWidth / 5f to 4
            Period.Last6Months -> virtualWidth / 6f to 5
            is Period.Custom, Period.Year -> virtualWidth / 12f to 11
        }
        (1..verticalLines).forEach { index ->
            val x = offset.x + index * stepWidth
            drawLine(
                color = lineColor.copy(alpha = 0.5f),
                start = Offset(x, offset.y),
                end = Offset(x, size.height - offset.y),
                strokeWidth = strokeWidth,
                pathEffect = pathEffect
            )
        }

        // horizontal lines
        val (stepHeights, horizontalLines) = virtualHeight / graphValuesRange.steps to graphValuesRange.steps - 1
        (1..horizontalLines).forEach { index ->
            val y = offset.y + index * stepHeights
            drawLine(
                color = lineColor.copy(alpha = 0.5f),
                start = Offset(offset.x, y),
                end = Offset(size.width - offset.x - textBounds.width(), y),
                strokeWidth = strokeWidth,
                pathEffect = pathEffect
            )
        }
    }
}

private fun gridTexts(
    drawScope: DrawScope,
    period: Period,
    graphValuesRange: GraphValuesRange,
    utilData: UtilData,
    virtualWidth: Float,
    virtualHeight: Float,
    daysInMonth: Int,
    currentMonth: Int
) {
    val textBounds = utilData.textBounds
    val textPaint = utilData.textPaint

    with(drawScope) {
        // abscissa text
        val (stepWidth, abscissa) = when (period) {
            Period.Day -> virtualWidth / 4f to abscissa(Period.Day)
            Period.Week -> virtualWidth / 7f to abscissa(Period.Week)
            Period.Month -> virtualWidth / 5f to abscissa(
                Period.Month, currentMonth, daysInMonth
            )
            Period.Last6Months -> virtualWidth / 6f to abscissa(
                Period.Last6Months, currentMonth, daysInMonth
            )
            is Period.Custom, Period.Year -> virtualWidth / 12f to abscissa(Period.Year)

        }
        abscissa.forEachIndexed { index, text ->
            drawIntoCanvas {
                it.nativeCanvas.drawText(
                    text,
                    utilData.offset.x + utilData.offset.x * 0.2f + index * stepWidth,
                    size.height - textBounds.height(),
                    textPaint
                )
            }
        }
        // ordinate text
        val stepHeight = virtualHeight / graphValuesRange.steps
        val ordinate = mutableListOf<String>()
        for (range in graphValuesRange.max downTo graphValuesRange.min step (graphValuesRange.max - graphValuesRange.min) / graphValuesRange.steps) {
            ordinate.add(graphValuesRange.rangeToTxt(range))
        }
        ordinate.forEachIndexed { index, text ->
            drawIntoCanvas {
                it.nativeCanvas.drawText(
                    text,
                    size.width - utilData.offset.x - textBounds.width() + utilData.offset.x * 0.2f,
                    utilData.offset.y + index * stepHeight + textBounds.height() / 2f,
                    textPaint
                )
            }
        }
    }

}

private fun abscissa(
    period: Period,
    currentMonth: Int = 1,
    daysInMonth: Int = 0
): Array<String> {
    return when (period) {
        Period.Day -> arrayOf("00:00", "06:00", "12:00", "18:00")
        Period.Week -> DayOfWeek
            .values()
            .map { week ->
                week.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            }
            .toTypedArray()
        Period.Month -> {
            val step = daysInMonth / 6
            arrayOf(
                "1",
                (2 * step).toString(),
                (3 * step).toString(),
                (4 * step).toString(),
                (5 * step).toString()
            )
        }
        Period.Last6Months -> {
            var index = 6
            var tmpCurMonth = currentMonth
            val displayMonths: MutableList<Month> = mutableListOf()
            val months = Month.values()
            while (index > 0) {
                displayMonths.add(months[tmpCurMonth - 1])
                tmpCurMonth--
                index--
                if (tmpCurMonth == 0) {
                    tmpCurMonth = 12
                }
            }
            displayMonths.reverse()
            displayMonths
                .map { m ->
                    m.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                }
                .toTypedArray()
        }
        is Period.Custom, Period.Year -> Month
            .values()
            .map { it ->
                it.getDisplayName(TextStyle.NARROW, Locale.getDefault())
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            }
            .toTypedArray()

    }
}

private fun data(
    drawScope: DrawScope,
    utilData: UtilData,
    virtualWidth: Float,
    virtualHeight: Float,
    data: List<GraphSection>,
    graphValuesRange: GraphValuesRange,
    legends: Map<Int, Pair<Painter, Color>>
) {
    if (data.isEmpty() || legends.isEmpty()) return
    with(drawScope) {
        val stepWidth = virtualWidth / data.size
        val stepHeight = virtualHeight / (graphValuesRange.max - graphValuesRange.min)
        data.forEachIndexed { index, section ->
            if (section is GraphSection.Section) {
                val activatedTypes = legends.keys
                section.points.filter { activatedTypes.contains(it.type) }
                    .sortedBy { -it.drawOrder }.forEach { point ->
                    val legendItem = legends[point.type]!!
                    val maxValue = graphValuesRange.max
                    val radius = 10f

                    val x = index * stepWidth + utilData.offset.x
                    val y = stepHeight * (maxValue - point.max) + utilData.offset.y - radius / 2f
                    val y2 = stepHeight * (maxValue - point.min) + utilData.offset.y - radius / 2f

                    if (point.min != point.max) {
                        drawLine(
                            color = legendItem.second.copy(alpha = 0.15f),
                            start = Offset(x + radius, y),
                            end = Offset(x + radius, y2),
                            strokeWidth = radius * 2f
                        )
                        translate(x, y2 - radius) {
                            with(legendItem.first) {
                                draw(
                                    size = Size(2 * radius, 2 * radius),
                                    colorFilter = ColorFilter.tint(legendItem.second)
                                )
                            }
                        }

                    }
                    translate(x, y - radius) {
                        with(legendItem.first) {
                            draw(
                                size = Size(2 * radius, 2 * radius),
                                colorFilter = ColorFilter.tint(legendItem.second)
                            )
                        }
                    }
                }
            }
        }
    }
}

private data class UtilData(
    val offset: Offset,
    val textBounds: Rect,
    val lineColor: Color,
    val strokeWidth: Float,
    val textPaint: NativePaint
)

@Preview(name = "Preview", showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    AppTheme {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            val period: MutableState<Period> = remember { mutableStateOf(Period.Day) }
            Graph(
                modifier = Modifier
                    .background(color = MaterialTheme.colors.measurableBG),
                period = period.value,
                anchorDate = AnchorDate(12, 3, 2022, 31),
                graphValuesRange = GraphValuesRange(max = 250, min = 20, steps = 3),
                graphLegendTypes = listOf(
                    GraphLegendDto(GraphLegendType.SYSTOLIC, true),
                    GraphLegendDto(GraphLegendType.DIASTOLIC, true),
                    GraphLegendDto(GraphLegendType.HEART_RATE, false),
                )
            )
        }
    }
}