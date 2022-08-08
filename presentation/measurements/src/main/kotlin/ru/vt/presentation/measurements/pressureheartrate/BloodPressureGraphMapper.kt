package ru.vt.presentation.measurements.pressureheartrate

import ru.vt.domain.measurement.entity.BloodPressureMinMaxPeriodEntity
import ru.vt.presentation.measurements.common.entity.GraphLegendType
import ru.vt.presentation.measurements.common.entity.GraphSection
import ru.vt.presentation.measurements.common.entity.GraphSectionPoint
import timber.log.Timber

internal object BloodPressureGraphMapper {
    fun map(
        data: List<BloodPressureMinMaxPeriodEntity>
    ): List<GraphSection> {
        if (data.isEmpty()) return emptyList()
        return try {
            val result = mutableListOf<GraphSection>()
            data.forEach { item ->
                if (item.systolicMax == 0) {
                    result.add(GraphSection.Empty(item.bound))
                } else {
                    val points = mutableListOf(
                        mapToSystolicPoint(item.systolicMin, item.systolicMax),
                        mapToDiastolicPoint(item.diastolicMin, item.diastolicMax)
                    )
                    if (item.heartRateMax != null && item.heartRateMin != null) {
                        points.add(mapToHeartRatePoint(item.heartRateMin!!, item.heartRateMax!!))
                    }
                    result.add(GraphSection.Section(range = item.bound, points = points))
                }
            }
            result
        } catch (e: Exception) {
            Timber.e(e)
            emptyList()
        }
    }

    private fun mapToSystolicPoint(min: Int, max: Int): GraphSectionPoint = GraphSectionPoint(
        min = min,
        max = max,
        type = GraphLegendType.SYSTOLIC,
        drawOrder = 0
    )

    private fun mapToDiastolicPoint(min: Int, max: Int): GraphSectionPoint = GraphSectionPoint(
        min = min,
        max = max,
        type = GraphLegendType.DIASTOLIC,
        drawOrder = 1
    )

    private fun mapToHeartRatePoint(min: Int, max: Int): GraphSectionPoint = GraphSectionPoint(
        min = min,
        max = max,
        type = GraphLegendType.HEART_RATE,
        drawOrder = 2
    )
}
