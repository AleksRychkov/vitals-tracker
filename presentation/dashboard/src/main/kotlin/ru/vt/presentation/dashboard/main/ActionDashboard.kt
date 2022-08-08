package ru.vt.presentation.dashboard.main

import ru.vt.domain.common.Measurement

internal sealed class ActionDashboard {
    data class Init(val profileId: Long) : ActionDashboard()

    data class UpdateTrackedMeasurement(
        @Measurement val type: Int,
        val isTracked: Boolean
    ) : ActionDashboard()

    data class NavigateToMeasurement(@Measurement val type: Int) : ActionDashboard()
}
