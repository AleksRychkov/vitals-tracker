package ru.vt.presentation.measurements.headaches.summary.ui

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import ru.vt.core.resources.R
import ru.vt.core.ui.compose.theme.Medium
import ru.vt.core.ui.compose.utils.textResource
import ru.vt.domain.common.MeasurementParams
import ru.vt.domain.measurement.entity.HeadacheArea
import ru.vt.domain.measurement.entity.HeadacheEntity
import ru.vt.presentation.measurements.common.compose.EntityDataItem
import ru.vt.presentation.measurements.common.utils.toStringTime

@Composable
internal fun EntityItem(
    modifier: Modifier,
    index: Int,
    entity: HeadacheEntity,
    deleteEntity: (HeadacheEntity) -> Unit
) {
    EntityDataItem(
        modifier = modifier,
        index = index,
        entity = entity,
        deleteEntity = deleteEntity
    ) {
        val text = entity.timestamp.toStringTime()
        Text(
            modifier = Modifier.padding(bottom = Medium),
            text = text,
            style = MaterialTheme.typography.subtitle2.copy(color = MaterialTheme.colors.secondaryVariant),
            fontStyle = FontStyle.Italic
        )
        Row(
            modifier = Modifier.height(IntrinsicSize.Max),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "${entity.intensity}",
                style = MaterialTheme.typography.subtitle1.copy(color = MaterialTheme.colors.primary)
            )
            Text(
                modifier = Modifier.padding(start = Medium),
                text = textResource(key = textResource(key = MeasurementParams.HEADACHE_INTENSITY.key)).lowercase(),
                style = MaterialTheme.typography.subtitle2.copy(color = MaterialTheme.colors.secondaryVariant)
            )
        }
        if (entity.headacheArea != HeadacheArea.UNDEFINED) {
            Row(
                modifier = Modifier
                    .padding(top = Medium)
                    .height(IntrinsicSize.Max),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = textResource(key = entity.headacheArea.key),
                    style = MaterialTheme.typography.subtitle1.copy(color = MaterialTheme.colors.primary)
                )
                Text(
                    modifier = Modifier.padding(start = Medium),
                    text = textResource(id = R.string.screen_create_headaches_area).lowercase(),
                    style = MaterialTheme.typography.subtitle2.copy(color = MaterialTheme.colors.secondaryVariant)
                )
            }
        }
    }
}
