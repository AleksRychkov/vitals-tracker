package ru.vt.presentation.measurements.weight.summary

import ru.vt.domain.measurement.entity.WeightMinMaxPeriodEntity
import ru.vt.presentation.measurements.common.entity.GraphLegendType
import ru.vt.presentation.measurements.common.entity.GraphSection
import ru.vt.presentation.measurements.common.entity.GraphSectionPoint
import timber.log.Timber

internal object WeightGraphMapper {
    fun map(
        data: List<WeightMinMaxPeriodEntity>
    ): List<GraphSection> {
        if (data.isEmpty()) return emptyList()
        return try {
            val result = mutableListOf<GraphSection>()
            data.forEach { item ->
                if (item.weightMax == 0) {
                    result.add(GraphSection.Empty(item.bound))
                } else {
                    result.add(
                        GraphSection.Section(
                            range = item.bound,
                            points = listOf(mapToPoint(item.weightMax, item.weightMin))
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
        type = GraphLegendType.WEIGHT,
        drawOrder = 0
    )
}
