package ru.vt.core.ui.compose.components.picker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import ru.vt.core.ui.compose.components.WheelComposable
import ru.vt.core.ui.compose.utils.textResource
import ru.vt.core.ui.compose.theme.Normal

@Composable
fun DatePickerComposable(
    modifier: Modifier,
    date: Triple<Int, Int, Int>? = null, // day, month, year
    localDate: LocalDate? = null,
    onDateSelected: (Triple<Int, Int, Int>) -> Unit
) {
    val today: LocalDate = date?.let { LocalDate.of(it.third, it.second, it.first) }
        ?: localDate
        ?: LocalDate.now()

    val days: MutableState<List<Int>> =
        remember { mutableStateOf((1..today.lengthOfMonth()).toList()) }
    val months: List<Month> = Month.values().toList()
    val years: List<Int> = (1900..2100).toList()

    val values = remember {
        mutableStateOf(Triple(today.dayOfMonth, today.month, today.year))
    }

    val onDateChanged: (Triple<Int, Month, Int>) -> Unit = { t: Triple<Int, Month, Int> ->
        var day: Int = t.first
        val month: Month = t.second
        val year: Int = t.third

        val d = LocalDate.of(year, month, 1)
        val maxDaysInMonth = d.lengthOfMonth()

        val current = values.value
        if (current != t) {
            days.value = (1..maxDaysInMonth).toList()
            if (day > maxDaysInMonth) {
                day = maxDaysInMonth
            }
            val newT = t.copy(first = day)
            values.value = newT
            onDateSelected(Triple(day, month.value, year))
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(Normal)
    ) {
        val itemHeight: Dp = 30.dp
        DateWheels(
            modifier = modifier,
            itemHeight = itemHeight,
            values = derivedStateOf { values.value }.value,
            days = derivedStateOf { days.value }.value,
            months = months,
            years = years,
            onDateChanged = onDateChanged
        )
        PickerIndicator(boxScope = this, itemHeight = itemHeight)
    }
}

@Composable
private fun DateWheels(
    modifier: Modifier,
    itemHeight: Dp,
    values: Triple<Int, Month, Int>,
    days: List<Int>,
    months: List<Month>,
    years: List<Int>,
    onDateChanged: (Triple<Int, Month, Int>) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        WheelComposable(
            modifier = Modifier.weight(1f),
            itemHeight = itemHeight,
            value = values.first,
            data = days,
            onItemSelected = {
                onDateChanged(values.copy(first = it))
            }
        )
        WheelComposable(
            modifier = Modifier.weight(1f),
            itemHeight = itemHeight,
            value = values.second,
            data = months,
            labelAsString = {
                textResource(key = this.toString())
            },
            onItemSelected = {
                onDateChanged(values.copy(second = it))
            }
        )
        WheelComposable(
            modifier = Modifier.weight(1f),
            itemHeight = itemHeight,
            value = values.third,
            data = years,
            onItemSelected = {
                onDateChanged(values.copy(third = it))
            }
        )
    }
}
