package ru.vt.presentation.measurements.common.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import ru.vt.core.resources.R
import ru.vt.core.ui.compose.theme.Medium
import ru.vt.core.ui.compose.theme.Normal
import ru.vt.core.ui.compose.utils.textResource

@Composable
internal fun NoDataItem(modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .background(color = MaterialTheme.colors.background)
            .padding(vertical = Medium, horizontal = Normal),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = textResource(id = R.string.no_data),
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.subtitle1.copy(color = MaterialTheme.colors.secondaryVariant)
        )
    }
}
