package ru.vt.presentation.measurements.common

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import ru.vt.domain.measurement.exceptions.InvalidPeriodValuesException
import ru.vt.presentation.measurements.common.entity.Period
import ru.vt.presentation.measurements.common.entity.getFromToValues

internal class PeriodTest {

    @Test
    fun `check Day period`() {
        val p = Period.Day

        val date = LocalDateTime.of(2022, 3, 1, 11, 34)

        val (from, to) = p.getFromToValues(date = date.toLocalDate())

        val expectedFrom = 1646082000000
        val expectedTo = 1646168400000

        assertEquals(expectedFrom, from)
        assertEquals(expectedTo, to)
    }

    @Test
    fun `check Week period`() {
        val p = Period.Week

        val date = LocalDateTime.of(2022, 3, 3, 11, 34)

        val (from, to) = p.getFromToValues(date = date.toLocalDate())

        val expectedFrom = 1645995600000
        val expectedTo = 1646600400000

        assertEquals(expectedFrom, from)
        assertEquals(expectedTo, to)
    }

    @Test
    fun `check Month period`() {
        val p = Period.Month

        val date = LocalDateTime.of(2022, 4, 3, 11, 34)

        val (from, to) = p.getFromToValues(date = date.toLocalDate())

        val expectedFrom = 1648760400000
        val expectedTo = 1651352400000

        assertEquals(expectedFrom, from)
        assertEquals(expectedTo, to)
    }

    @Test
    fun `check Last6Months period`() {
        val p = Period.Last6Months

        val date = LocalDateTime.of(2022, 4, 3, 11, 34)

        val (from, to) = p.getFromToValues(date = date.toLocalDate())

        val expectedFrom = 1635714000000
        val expectedTo = 1651352400000

        assertEquals(expectedFrom, from)
        assertEquals(expectedTo, to)
    }


    @Test
    fun `check Year period`() {
        val p = Period.Year

        val date = LocalDateTime.of(2022, 4, 3, 11, 34)

        val (from, to) = p.getFromToValues(date = date.toLocalDate())

        val expectedFrom = 1640984400000
        val expectedTo = 1672520400000

        assertEquals(expectedFrom, from)
        assertEquals(expectedTo, to)
    }

    @Test
    fun `should throw InvalidCustomPeriodValuesException when from is bigger then to for Custom period`() {
        val p = Period.Custom(System.currentTimeMillis(), System.currentTimeMillis() - 100)

        assertThrows<InvalidPeriodValuesException> {
            p.getFromToValues(LocalDate.now())
        }
    }

}