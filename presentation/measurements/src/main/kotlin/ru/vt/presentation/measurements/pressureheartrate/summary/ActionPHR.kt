package ru.vt.presentation.measurements.pressureheartrate.summary

import ru.vt.domain.measurement.entity.BloodPressureEntity
import ru.vt.presentation.measurements.common.entity.Period

internal sealed class ActionPHR {
    data class Init(val profileId: Long) : ActionPHR()
    data class SetPeriod(val period: Period) : ActionPHR()
    data class DeleteEntity(val entity: BloodPressureEntity) : ActionPHR()
    object OnBackPressed : ActionPHR()
    object Create: ActionPHR()
    object LoadMore: ActionPHR()
}
