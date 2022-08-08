package ru.vt.core.ui.compose.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.vt.core.ui.compose.theme.AppTheme

@Composable
fun AvatarComposable(modifier: Modifier, letter: String, onClick: () -> Unit = {}) {
    Box(modifier = modifier
        .defaultMinSize(36.dp, 36.dp)
        .clip(CircleShape)
        .drawWithContent {
            val gradient = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFA6AAB9),
                    Color(0xFF878C96)
                ),
                startY = 0f,
                endY = size.height
            )
            drawRect(
                gradient,
                blendMode = BlendMode.Multiply
            )
            drawContent()
        }
        .clickable { onClick() },
        contentAlignment = Alignment.Center
    )
    {
        Text(
            text = letter,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.primary
        )
    }
}

@Preview("Avatar Preview", showSystemUi = true, showBackground = true)
@Composable
fun AvatarPreview() {
    AppTheme {
        Column {
            AvatarComposable(letter = "A", modifier = Modifier.size(100.dp, 100.dp))
        }
    }
}