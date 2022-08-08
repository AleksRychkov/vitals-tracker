package ru.vt.data.source.db

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.vt.database.entity.DashboardEntityDb
import ru.vt.domain.common.Measurement
import ru.vt.domain.common.indexOfFirst
import ru.vt.domain.common.indexOfLast
import ru.vt.domain.dashboard.entity.DashboardEntity
import ru.vt.domain.dashboard.entity.TrackedEntity

internal class DBDashboardDataSourceMapperTest {

    @Test
    fun `should successfully convert db entity to domain entity when trackedItems are empty`() {
        val expected = DashboardEntity(
            profileId = 123L,
            trackedItems = (Measurement.indexOfFirst()..Measurement.indexOfLast()).map {
                TrackedEntity(it, false)
            }
        )

        val actual = DashboardEntityDb(
            profileId = 123L,
            trackedItems = ""
        )
        assertEquals(expected, DBDashboardDataSource.Mapper.mapFromDB(actual))
    }

    @Test
    fun `should successfully convert db entity to domain entity when trackedItems are not empty`() {
        val expected = DashboardEntity(
            profileId = 123L,
            trackedItems = (Measurement.indexOfFirst()..Measurement.indexOfLast()).map {
                TrackedEntity(it, it == Measurement.HEADACHE)
            }
        )

        val actual = DashboardEntityDb(
            profileId = 123L,
            trackedItems = Measurement.HEADACHE.toString()
        )
        assertEquals(expected, DBDashboardDataSource.Mapper.mapFromDB(actual))
    }
}
