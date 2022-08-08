package ru.vt.presentation.measurements.common.compose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.vt.core.resources.R
import ru.vt.core.ui.compose.theme.Medium
import ru.vt.core.ui.compose.theme.Normal
import ru.vt.core.ui.compose.theme.divider
import ru.vt.core.ui.compose.utils.dpToPx
import ru.vt.core.ui.compose.utils.noRippleClickable
import ru.vt.core.ui.compose.utils.textResource
import kotlin.math.roundToInt

@Composable
internal fun <Entity> EntityDataItem(
    modifier: Modifier,
    index: Int,
    entity: Entity,
    deleteEntity: (Entity) -> Unit,
    content: @Composable Entity.() -> Unit
) {
    val removeProgress = remember { Animatable(0f) }
    val screenWidth: Int
    with(LocalConfiguration.current) {
        screenWidth = this.screenWidthDp
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .graphicsLayer {
                this.translationX = -1f * removeProgress.value * screenWidth
                this.alpha = (1f - removeProgress.value)
            }
    ) {
        val coroutineScope = rememberCoroutineScope()
        val deleteBoxWidth = 100.dp
        val initialDeleteOffset = dpToPx(dp = deleteBoxWidth)
        val deleteOffsetX = remember { Animatable(initialDeleteOffset) }

        Column(
            modifier = modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(vertical = Medium, horizontal = Normal)
                .offset {
                    IntOffset(
                        (-(initialDeleteOffset - deleteOffsetX.value)).roundToInt(),
                        0
                    )
                }
                .noRippleClickable {
                    if (deleteOffsetX.value == 0f) {
                        coroutineScope.launch {
                            deleteOffsetX.animateTo(
                                initialDeleteOffset,
                                tween(easing = FastOutSlowInEasing)
                            )
                        }
                    }
                }
        ) {
            content(entity)
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(width = deleteBoxWidth)
                .offset { IntOffset(deleteOffsetX.value.roundToInt(), 0) }
                .background(color = MaterialTheme.colors.error)
                .align(Alignment.CenterEnd)
                .clickable {
                    if (removeProgress.isRunning || deleteOffsetX.isRunning) return@clickable
                    coroutineScope.launch {
                        removeProgress.animateTo(1f, tween()) {
                            if (this.value >= 1f) {
                                deleteEntity(entity)
                            }
                        }
                    }
                }
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = textResource(id = R.string.delete),
                style = MaterialTheme.typography.subtitle1.copy(color = Color.White)
            )
        }
        Icon(
            modifier = Modifier
                .size(42.dp, 42.dp)
                .align(Alignment.TopEnd)
                .offset {
                    IntOffset(
                        (-(initialDeleteOffset - deleteOffsetX.value)).roundToInt(),
                        0
                    )
                }
                .clickable {
                    if (removeProgress.isRunning) return@clickable
                    coroutineScope.launch {
                        if (deleteOffsetX.value > 0) {
                            0f
                        } else {
                            initialDeleteOffset
                        }.let {
                            deleteOffsetX.animateTo(it, tween(easing = FastOutSlowInEasing))
                        }
                    }
                }
                .padding(vertical = Medium, horizontal = Normal),
            painter = rememberVectorPainter(image = Icons.Filled.Delete),
            contentDescription = "",
            tint = MaterialTheme.colors.primary
        )
        if (index != 0) {
            Divider(
                modifier = Modifier.fillMaxWidth(),
                startIndent = 0.dp,
                color = MaterialTheme.colors.divider
            )
        }
    }
}
