package ru.vt.presentation.dashboard.main.measurements.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import ru.vt.core.resources.R
import ru.vt.core.ui.compose.theme.Medium
import ru.vt.core.ui.compose.theme.Normal
import ru.vt.core.ui.compose.utils.textResource

@Composable
internal fun CommonItem(
    onItemClick: () -> Unit,
    icon: Painter,
    iconTint: Color,
    title: String,
    titleColor: Color,
    isStateEmpty: Boolean,
    body: @Composable (ColumnScope) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .background(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colors.surface
            )
            .clip(MaterialTheme.shapes.medium)
            .clipToBounds()
            .clickable {
                onItemClick()
            }
            .padding(Normal)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                painter = icon,
                contentDescription = "",
                tint = iconTint
            )
            Text(
                modifier = Modifier
                    .padding(start = Medium)
                    .weight(1f, fill = true),
                text = title,
                style = MaterialTheme.typography.body1.copy(color = titleColor)
            )
            Icon(
                modifier = Modifier.size(16.dp),
                painter = rememberVectorPainter(image = Icons.Filled.ChevronRight),
                contentDescription = "",
                tint = MaterialTheme.colors.secondaryVariant
            )
        }

        if (isStateEmpty.not()) {
            body(this)
        }

        if (isStateEmpty) {
            Text(
                modifier = Modifier.padding(top = Medium),
                text = textResource(id = R.string.no_data),
                style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.secondaryVariant)
            )
        }
    }
}

@Composable
internal fun TrackedTime(
    modifier: Modifier,
    time: String
) {
    Text(
        modifier = modifier.padding(top = Medium),
        text = "${textResource(id = R.string.tracked_at)} $time",
        fontStyle = FontStyle.Italic,
        style = MaterialTheme.typography.subtitle2.copy(color = MaterialTheme.colors.secondaryVariant)
    )
}
