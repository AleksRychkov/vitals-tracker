package ru.vt.presentation.dashboard.main.measurements.common

import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

internal object Utils {
    fun convertDate(timestamp: Long, pattern: String = "dd MMM HH:mm"): String {
        val localDate =
            Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime()
        val dateFormatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
        return localDate.format(dateFormatter)
    }
}