package ru.vt.core.ui.compose.components.picker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
internal fun PickerIndicator(
    boxScope: BoxScope,
    itemHeight: Dp
) {
    with(boxScope) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight)
                .background(
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
                    shape = MaterialTheme.shapes.medium
                )
                .align(Alignment.Center)
        )
    }
}