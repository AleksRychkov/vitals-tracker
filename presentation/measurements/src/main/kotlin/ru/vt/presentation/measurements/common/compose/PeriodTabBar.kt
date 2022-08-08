package ru.vt.presentation.measurements.common.compose

import androidx.annotation.StringRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.vt.core.resources.R
import ru.vt.core.ui.compose.utils.pxToDp
import ru.vt.core.ui.compose.utils.textResource
import ru.vt.core.ui.compose.theme.*
import ru.vt.core.ui.compose.utils.noRippleClickable
import ru.vt.presentation.measurements.common.entity.Period

@Composable
internal fun PeriodTabBar(
    modifier: Modifier,
    selected: Period,
    onPeriodSelected: (Period) -> Unit
) {
    val tabBarShape = RoundedCornerShape(10.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .defaultMinSize(minHeight = 36.dp)
            .clip(tabBarShape)
            .background(
                shape = tabBarShape,
                color = MaterialTheme.colors.tabBar
            )
    ) {
        val coroutineScope = rememberCoroutineScope()
        val selectorSize: MutableState<IntSize> = remember { mutableStateOf(IntSize.Zero) }
        val offsetX = remember { Animatable(-1f) }
        val onSelected: (Period, Float) -> Unit = { period, offset ->
            if (offsetX.isRunning.not()) {
                onPeriodSelected(period)
                coroutineScope.launch {
                    offsetX.animateTo(offset, animationSpec = tween(easing = FastOutSlowInEasing))
                }
            }
        }
        val sizeCallback: (Int, Float) -> Unit = { width, offset ->
            selectorSize.value = IntSize(width, 0)
            coroutineScope.launch {
                offsetX.snapTo(offset)
            }
        }

        if (selectorSize.value != IntSize.Zero) {
            val size = selectorSize.value
            Surface(
                modifier = Modifier
                    .width(width = pxToDp(px = size.width) + 4.dp)
                    .fillMaxHeight()
                    .offset { IntOffset(offsetX.value.toInt(), 0) }
                    .padding(vertical = 3.dp, horizontal = 4.dp),
                elevation = 4.dp,
                shape = tabBarShape
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = MaterialTheme.colors.tabBarSelector,
                            shape = tabBarShape
                        )
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PeriodText(
                resId = R.string.period_day,
                rowScope = this,
                period = Period.Day,
                sizeCallback = if (offsetX.value == -1f && selected == Period.Day) sizeCallback else null,
                onSelected = onSelected,
            )

            PeriodDelimiter(selected, Period.Day, Period.Week, offsetX.isRunning)

            PeriodText(
                resId = R.string.period_week,
                rowScope = this,
                period = Period.Week,
                sizeCallback = if (offsetX.value == -1f && selected == Period.Week) sizeCallback else null,
                onSelected = onSelected,
            )

            PeriodDelimiter(selected, Period.Week, Period.Month, offsetX.isRunning)

            PeriodText(
                resId = R.string.period_month,
                rowScope = this,
                period = Period.Month,
                sizeCallback = if (offsetX.value == -1f && selected == Period.Month) sizeCallback else null,
                onSelected = onSelected,
            )

            PeriodDelimiter(selected, Period.Month, Period.Last6Months, offsetX.isRunning)

            PeriodText(
                resId = R.string.period_half_year,
                rowScope = this,
                period = Period.Last6Months,
                sizeCallback = if (offsetX.value == -1f && selected == Period.Last6Months) sizeCallback else null,
                onSelected = onSelected,
            )

            PeriodDelimiter(selected, Period.Last6Months, Period.Year, offsetX.isRunning)

            PeriodText(
                resId = R.string.period_year,
                rowScope = this,
                period = Period.Year,
                sizeCallback = if (offsetX.value == -1f && selected == Period.Year) sizeCallback else null,
                onSelected = onSelected,
            )
        }
    }
}

@Composable
private fun PeriodText(
    @StringRes resId: Int,
    rowScope: RowScope,
    period: Period,
    sizeCallback: ((Int, Float) -> Unit)? = null,
    onSelected: (Period, Float) -> Unit = { _, _ -> }
) {
    val offsetX = remember { mutableStateOf<Float?>(null) }
    with(rowScope) {
        Text(
            text = textResource(id = resId),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.primary),
            modifier = Modifier
                .weight(weight = 1f, fill = true)
                .onGloballyPositioned {
                    sizeCallback?.invoke(it.size.width, it.positionInParent().x)
                    if (offsetX.value == null) {
                        offsetX.value = it.positionInParent().x
                    }
                }
                .noRippleClickable {
                    if (offsetX.value != null) {
                        onSelected(period, offsetX.value!!)
                    }
                }
        )
    }
}

@Composable
private fun PeriodDelimiter(
    selected: Period,
    left: Period,
    right: Period,
    isRunning: Boolean
) {
    val color =
        if ((selected != left && selected != right) || isRunning) MaterialTheme.colors.divider else Color.Transparent
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(1.dp)
            .padding(vertical = 8.dp)
            .background(color = color)
    )
}

@Preview(name = "Preview", showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    AppTheme {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            val period: MutableState<Period> = remember { mutableStateOf(Period.Week) }
            PeriodTabBar(
                modifier = Modifier.padding(vertical = Normal, horizontal = Large),
                period.value
            ) {
                period.value = it
            }
        }
    }
}
