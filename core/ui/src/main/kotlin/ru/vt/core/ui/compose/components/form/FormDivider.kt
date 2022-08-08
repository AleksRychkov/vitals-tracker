package ru.vt.core.ui.compose.components.form

import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import ru.vt.core.ui.compose.theme.Normal
import ru.vt.core.ui.compose.theme.divider

@Composable
fun FormDivider(
    modifier: Modifier = Modifier,
    startIndent: Dp = Normal,
    color: Color = MaterialTheme.colors.divider
) {
    Divider(
        modifier = modifier,
        startIndent = startIndent,
        color = color
    )
}