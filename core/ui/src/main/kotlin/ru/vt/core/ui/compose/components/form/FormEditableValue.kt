package ru.vt.core.ui.compose.components.form

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

@Composable
fun FormEditableValue(
    text: String,
    color: Color = MaterialTheme.colors.primaryVariant,
) {
    Text(
        modifier = Modifier.fillMaxHeight(),
        textAlign = TextAlign.Center,
        text = text,
        color = color,
        style = MaterialTheme.typography.subtitle1
    )
}