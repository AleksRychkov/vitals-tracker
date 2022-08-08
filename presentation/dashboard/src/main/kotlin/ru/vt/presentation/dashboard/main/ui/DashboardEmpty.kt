package ru.vt.presentation.dashboard.main.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.vt.core.resources.R
import ru.vt.core.ui.compose.utils.textResource
import ru.vt.core.ui.compose.theme.Normal
import ru.vt.core.ui.compose.theme.Normal2X
import ru.vt.core.ui.compose.theme.divider
import java.lang.Float.max
import kotlin.math.min

@Composable
internal fun EmptyDashboard(
    editTextPosition: State<Offset?>
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .height(IntrinsicSize.Max)) {
        if (editTextPosition.value != null) {
            val color = MaterialTheme.colors.divider
            val canvasPos = remember { mutableStateOf<Offset?>(null) }
            Canvas(modifier = Modifier
                .fillMaxWidth()
                .height(128.dp)
                .onGloballyPositioned {
                    if (canvasPos.value == null) {
                        val rect = it.boundsInRoot()
                        canvasPos.value = rect.topCenter
                    }
                }, onDraw = {
                if (canvasPos.value != null && editTextPosition.value != null) {
                    val referencePos = editTextPosition.value!!
                    val localPos = canvasPos.value!!

                    val canvasWidth = size.width
                    val canvasHeight = size.height

                    val start = Offset(x = referencePos.x, y = -(localPos.y - referencePos.y))
                    val end = Offset(x = canvasWidth / 2f, y = canvasHeight)
                    val strokeWidth = 3f
                    drawCircle(
                        color = color,
                        center = start,
                        radius = 1.5f * strokeWidth,
                    )

                    val widthStartEnd = (max(start.x, end.x) - min(start.x, end.x)) / 2

                    val path2 = Path().apply {
                        moveTo(x = start.x, y = start.y)

                        cubicTo(
                            x1 = start.x - widthStartEnd * 4f,
                            y1 = start.y + (end.y - start.y) / 1.5f,
                            x2 = end.x + widthStartEnd * 4f,
                            y2 = start.y + (end.y - start.y) / 3,
                            x3 = end.x,
                            y3 = end.y
                        )
                    }
                    drawPath(
                        path = path2,
                        color = color,
                        style = Stroke(width = strokeWidth)
                    )
                }
            })
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = Normal2X)
                        .border(
                            width = 1.dp,
                            color = color,
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(Normal),
                    textAlign = TextAlign.Center,
                    text = textResource(id = R.string.screen_dashboard_empty_screen),
                    style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.secondaryVariant)
                )
            }
        }
    }
}
