package ru.vt.presentation.measurements.headaches.summary

import ru.vt.domain.measurement.entity.HeadacheEntity
import ru.vt.presentation.measurements.common.entity.Period

internal sealed class ActionHeadachesSummary {
    data class Init(val profileId: Long) : ActionHeadachesSummary()
    data class SetPeriod(val period: Period) : ActionHeadachesSummary()
    data class Delete(val entity: HeadacheEntity) : ActionHeadachesSummary()
    object LoadMore : ActionHeadachesSummary()
    object OnBackPressed : ActionHeadachesSummary()
    object Create : ActionHeadachesSummary()
}
