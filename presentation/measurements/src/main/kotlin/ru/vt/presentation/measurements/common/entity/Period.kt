package ru.vt.presentation.measurements.common.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import ru.vt.domain.measurement.exceptions.InvalidPeriodValuesException
import ru.vt.presentation.measurements.common.utils.toLocalDateTime
import ru.vt.presentation.measurements.common.utils.toMills
import java.util.concurrent.TimeUnit

internal sealed class Period : Parcelable {
    @Parcelize
    object Day : Period(), Parcelable

    @Parcelize
    object Week : Period(), Parcelable

    @Parcelize
    object Month : Period(), Parcelable

    @Parcelize
    object Last6Months : Period(), Parcelable

    @Parcelize
    object Year : Period(), Parcelable

    @Parcelize
    data class Custom(val from: Long, val to: Long) : Period(), Parcelable
}

internal fun Period.getFromToValues(date: LocalDate): Pair<Long, Long> {
    return when (this) {
        is Period.Day -> {
            val from = date.toMills()
            val to = date.plusDays(1).toMills()
            from to to
        }
        is Period.Week -> {
            val from = date.with(DayOfWeek.MONDAY).toMills()
            val to = date.plusWeeks(1).with(DayOfWeek.MONDAY).toMills()
            from to to
        }
        is Period.Month -> {
            val from = date.withDayOfMonth(1).toMills()
            val to = date.plusMonths(1).withDayOfMonth(1).toMills()
            from to to
        }
        is Period.Last6Months -> {
            val from = date.minusMonths(5).withDayOfMonth(1).toMills()
            val to = date.plusMonths(1).withDayOfMonth(1).toMills()
            from to to
        }
        is Period.Year -> {
            val from = date.withMonth(1).withDayOfMonth(1).toMills()
            val to = date.plusYears(1).withMonth(1).withDayOfMonth(1).toMills()
            from to to
        }
        is Period.Custom -> {
            if (this.from >= this.to) {
                throw InvalidPeriodValuesException
            }
            this.from to this.to
        }
    }
}

internal fun Period.getBounds(anchorDate: LocalDate): List<Pair<Long, Long>> {
    val from = this.getFromToValues(anchorDate).first
    val bounds = mutableListOf<Pair<Long, Long>>()
    when (this) {
        Period.Day -> {
            val stepInMills = TimeUnit.MINUTES.toMillis(60)
            (0..23).forEach { i ->
                bounds.add(Pair(from + i * stepInMills, from + i * stepInMills + stepInMills))
            }
        }
        Period.Week -> {
            val stepInMills = TimeUnit.DAYS.toMillis(1)
            (0..6).forEach { i ->
                val firstHalfOfDay = Pair(
                    from + i * stepInMills,
                    from + i * stepInMills + stepInMills / 2
                )
                val secondHalfOfDay = Pair(
                    from + i * stepInMills + stepInMills / 2,
                    from + i * stepInMills + stepInMills
                )
                bounds.add(firstHalfOfDay)
                bounds.add(secondHalfOfDay)
            }
        }
        Period.Month -> {
            val stepInMills = TimeUnit.DAYS.toMillis(1)
            val daysInMonth = anchorDate.month.length(anchorDate.isLeapYear)
            (0 until daysInMonth).forEach { i ->
                bounds.add(Pair(from + i * stepInMills, from + i * stepInMills + stepInMills))
            }
        }
        Period.Last6Months -> {
            val fromDate = from.toLocalDateTime()
            for (i in (0..5)) {
                val month = fromDate.plusMonths(i.toLong())
                val monthMs = month.toMills()
                val isLeap = fromDate.toLocalDate().isLeapYear
                val stepMs = TimeUnit.DAYS.toMillis(month.month.length(isLeap).toLong())

                val firstHalfOfMonth = Pair(monthMs, monthMs + stepMs / 2)
                val secondHalfOfMonth = Pair(monthMs + stepMs / 2, monthMs + stepMs)

                bounds.add(firstHalfOfMonth)
                bounds.add(secondHalfOfMonth)
            }
        }
        is Period.Custom, Period.Year -> {
            val firstDateOfYear = from.toLocalDateTime()
            for (i in (0..11)) {
                val month = firstDateOfYear.plusMonths(i.toLong())
                val monthMs = month.toMills()
                val isLeap = firstDateOfYear.toLocalDate().isLeapYear
                val stepMs = TimeUnit.DAYS.toMillis(month.month.length(isLeap).toLong())
                val firstHalfOfMonth = Pair(
                    monthMs,
                    monthMs + stepMs / 2
                )
                val secondHalfOfMonth = Pair(
                    monthMs + stepMs / 2,
                    monthMs + stepMs
                )
                bounds.add(firstHalfOfMonth)
                bounds.add(secondHalfOfMonth)
            }
        }
    }
    return bounds
}
