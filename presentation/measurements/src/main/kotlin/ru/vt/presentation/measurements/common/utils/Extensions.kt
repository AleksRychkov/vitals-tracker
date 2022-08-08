package ru.vt.presentation.measurements.common.utils

import androidx.compose.runtime.Composable
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import ru.vt.core.ui.compose.utils.textResource
import ru.vt.domain.common.AppUnits
import java.util.*

internal fun LocalDate.toMills() = this.atStartOfDay()
    .atZone(ZoneId.systemDefault())
    .toInstant()
    .toEpochMilli()

internal fun LocalDateTime.toMills() = this.atZone(ZoneId.systemDefault())
    .toInstant()
    .toEpochMilli()

internal fun Long.toLocalDateTime() =
    LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())


fun Long.toStringTime(): String {
    val localDate = this.toLocalDateTime()
    val pattern = "dd.MM.yy HH:mm"
    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    return localDate.format(dateFormatter)
}
