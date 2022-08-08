package ru.vt.presentation.measurements.common.entity

import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ru.vt.core.resources.R
import ru.vt.core.ui.compose.utils.textResource


@IntDef(
    GraphLegendType.SYSTOLIC,
    GraphLegendType.DIASTOLIC,
    GraphLegendType.HEART_RATE,
    GraphLegendType.HEADACHE_INTENSITY,
    GraphLegendType.WEIGHT
)
@Retention(AnnotationRetention.SOURCE)
internal annotation class GraphLegendType {
    companion object {
        const val SYSTOLIC: Int = 0
        const val DIASTOLIC: Int = 1
        const val HEART_RATE: Int = 2

        const val HEADACHE_INTENSITY: Int = 3

        const val WEIGHT: Int = 4
    }
}

internal data class GraphLegendDto(
    @GraphLegendType val type: Int,
    val activated: Boolean
)

internal data class GraphLegendItem(
    @DrawableRes val drawableRes: Int,
    val drawableColor: Color,
    @StringRes val textRes: Int = 0
)

internal object GraphLegendFactory {

    private val cache: MutableMap<Int, GraphLegendItem> = mutableMapOf()

    fun getForType(@GraphLegendType type: Int, isLight: Boolean): GraphLegendItem? =
        when (type) {
            GraphLegendType.SYSTOLIC -> {
                if (cache.containsKey(type).not()) {
                    cache[type] = GraphLegendItem(
                        R.drawable.ic_filled_circle,
                        if (isLight) Color(0xFFFB0E55) else Color(0xFFFC205F),
                        R.string.systolic
                    )
                }
                cache[type]!!
            }
            GraphLegendType.DIASTOLIC -> {
                if (cache.containsKey(type).not()) {
                    cache[type] = GraphLegendItem(
                        R.drawable.ic_filled_diamond,
                        if (isLight) Color.Black else Color.White,
                        R.string.diastolic
                    )
                }
                cache[type]!!
            }
            GraphLegendType.HEART_RATE -> {
                if (cache.containsKey(type).not()) {
                    cache[type] = GraphLegendItem(
                        R.drawable.ic_filled_triangle,
                        Color(0xFF9152FF),
                        R.string.heart_rate
                    )
                }
                cache[type]!!
            }
            GraphLegendType.HEADACHE_INTENSITY -> {
                if (cache.containsKey(type).not()) {
                    cache[type] = GraphLegendItem(
                        drawableRes = R.drawable.ic_filled_circle,
                        drawableColor = if (isLight) Color(0xFFB71C1C) else Color(0xFFEA5247),
                        R.string.headache_intensity
                    )
                }
                cache[type]!!
            }
            GraphLegendType.WEIGHT -> {
                if (cache.containsKey(type).not()) {
                    cache[type] = GraphLegendItem(
                        drawableRes = R.drawable.ic_filled_circle,
                        drawableColor = if (isLight) Color(0xFF0085FF) else Color(0xFF168FFF),
                        R.string.weight
                    )
                }
                cache[type]!!
            }
            else -> null
        }

    @Composable
    fun getUnits(@GraphLegendType type: Int): String = when (type) {
        GraphLegendType.SYSTOLIC, GraphLegendType.DIASTOLIC -> textResource(id = R.string.unit_blood_pressure)
        else -> textResource(id = R.string.unit_heart_rate)
    }
}
