package ru.vt.data.repositories

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import ru.vt.data.datasource.DashboardDataSource
import ru.vt.domain.dashboard.entity.DashboardEntity

@ExtendWith(MockKExtension::class)
internal class DashboardRepositoryImplTest {

    @MockK
    private lateinit var dashboardDataSource: DashboardDataSource

    @InjectMockKs
    private lateinit var repository: DashboardRepositoryImpl

    @Test
    fun `should create new dashboard when no dashboard for profileId stored`(): Unit = runBlocking {
        val profileId = 123L
        val entity = DashboardEntity(profileId = profileId)
        coEvery { dashboardDataSource.getDashboard(profileId) } returns null
        coEvery { dashboardDataSource.createDashboard(profileId) } returns entity

        assertEquals(entity, repository.getDashboard(profileId))

        coVerify(exactly = 1) { dashboardDataSource.createDashboard(profileId) }
    }
}
