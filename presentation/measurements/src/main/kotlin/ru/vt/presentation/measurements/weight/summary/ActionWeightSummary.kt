package ru.vt.presentation.measurements.weight.summary

import ru.vt.domain.measurement.entity.WeightEntity
import ru.vt.presentation.measurements.common.entity.Period

internal sealed class ActionWeightSummary {
    data class Init(val profileId: Long) : ActionWeightSummary()
    data class SetPeriod(val period: Period) : ActionWeightSummary()
    data class Delete(val entity: WeightEntity) : ActionWeightSummary()
    object LoadMore : ActionWeightSummary()
    object OnBackPressed : ActionWeightSummary()
    object Create : ActionWeightSummary()
}
