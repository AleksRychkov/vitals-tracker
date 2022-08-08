package ru.vt.presentation.measurements.headaches

import ru.vt.domain.measurement.entity.HeadacheMinMaxPeriodEntity
import ru.vt.presentation.measurements.common.entity.GraphLegendType
import ru.vt.presentation.measurements.common.entity.GraphSection
import ru.vt.presentation.measurements.common.entity.GraphSectionPoint
import timber.log.Timber

internal object HeadachesGraphMapper {
    fun map(
        data: List<HeadacheMinMaxPeriodEntity>
    ): List<GraphSection> {
        if (data.isEmpty()) return emptyList()
        return try {
            val result = mutableListOf<GraphSection>()
            data.forEach { item ->
                if (item.intensityMax == 0) {
                    result.add(GraphSection.Empty(item.bound))
                } else {
                    result.add(
                        GraphSection.Section(
                            range = item.bound,
                            points = listOf(mapToPoint(item.intensityMin, item.intensityMax))
                        )
                    )
                }
            }
            result
        } catch (e: Exception) {
            Timber.e(e)
            emptyList()
        }
    }

    private fun mapToPoint(min: Int, max: Int): GraphSectionPoint = GraphSectionPoint(
        max = max,
        min = min,
        type = GraphLegendType.HEADACHE_INTENSITY,
        drawOrder = 0
    )
}
