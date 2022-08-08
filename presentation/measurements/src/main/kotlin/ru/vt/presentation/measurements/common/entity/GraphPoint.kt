package ru.vt.presentation.measurements.common.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

internal sealed class GraphSection(
    open val range: Pair<Long, Long>
) : Parcelable {
    @Parcelize
    internal data class Section(
        override val range: Pair<Long, Long>,
        val points: List<GraphSectionPoint>
    ) : GraphSection(range = range), Parcelable

    @Parcelize
    internal data class Empty(
        override val range: Pair<Long, Long>
    ) : GraphSection(range = range), Parcelable
}

@Parcelize
internal data class GraphSectionPoint(
    @GraphLegendType val type: Int,
    val max: Int,
    val min: Int,
    val drawOrder: Int // lesser the value then the closer to the viewer
) : Parcelable
