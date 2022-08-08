package ru.vt.core.ui.compose.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.vt.core.ui.compose.LocalVibratorService
import ru.vt.core.ui.compose.utils.dpToPx
import ru.vt.core.ui.compose.utils.pxToDp
import kotlin.math.*


// todo: fix bug with overscroll!!
@Composable
fun <T> WheelComposable(
    modifier: Modifier = Modifier,
    data: List<T> = emptyList(),
    rows: Int = 7,
    value: T,
    itemHeight: Dp = 30.dp,
    labelAsString: @Composable T.() -> String = { this.toString() },
    onItemSelected: (T) -> Unit = {}
) {
    val itemHeightPx = dpToPx(dp = itemHeight)
    val virtualHeightPx = (data.size - 1) * itemHeightPx
    val actualHeightPx = itemHeightPx * rows
    val actualHeight =  pxToDp(px = actualHeightPx)
    val halfCircumference = actualHeightPx * Math.PI / 2f
    val radius = actualHeightPx / 2f
    val selectedIndex = data.indexOf(value)

    val coroutineScope = rememberCoroutineScope()
    val animatedOffset = remember { Animatable(0f) }
    var isDragging by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = value, key2 = data.joinToString(), block = {
        if (!animatedOffset.isRunning && isDragging.not()) {
            val offset = -data.indexOf(value) * itemHeightPx
            animatedOffset.snapTo(offset)
        }
    })

    val vibrator = LocalVibratorService.current

    Box(
        modifier = modifier
            .height(actualHeight)
            .clipToBounds()
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { deltaY ->
                    val oldOffset = animatedOffset.value
                    var offset = oldOffset
                    offset -= -deltaY
                    if (offset < itemHeightPx / 2f && abs(offset) <= virtualHeightPx + itemHeightPx / 2f) {
                    } else {
                        offset = oldOffset
                    }
                    coroutineScope.launch {
                        animatedOffset.snapTo(offset)

                        val currentIndex = abs(((offset / itemHeightPx) % (data.size)).roundToInt())
                        if (selectedIndex != currentIndex) {
                            val middle = actualHeightPx / 2 - itemHeightPx / 2
                            val t = abs((currentIndex * itemHeightPx + offset) / middle)
                            if (t < 0.1f) {
                                onItemSelected(data[currentIndex])
                                vibrator.performTick()
                            }
                        }
                    }
                },
                onDragStarted = {
                    isDragging = true
                },
                onDragStopped = { velocity ->
                    coroutineScope.launch {
                        val offset = animatedOffset.fling(
                            velocity, 0f, virtualHeightPx, itemHeightPx
                        ).endState.value
                        animatedOffset.snapTo(offset)

                        isDragging = false
                        val currentIndex = abs(((offset / itemHeightPx) % (data.size)).roundToInt())
                        if (selectedIndex != currentIndex) {
                            onItemSelected(data[currentIndex])
                            vibrator.performTick()
                        }
                    }
                }
            )
    ) {
        val middle = actualHeightPx / 2 - itemHeightPx / 2

        val offset = animatedOffset.value
        val currentIndex = abs(((offset / itemHeightPx) % (data.size)).roundToInt())

        val (from, to) = max(0, currentIndex - rows / 2 - 1) to
                min(data.size - 1, currentIndex + rows / 2 + 1)

        (from..to).map { i ->
            val index = i % (data.size)
            val text = data[index]
            val radian: Double = (index * itemHeightPx + offset) * Math.PI / halfCircumference
            val translateY: Float = middle + radius * sin(radian).toFloat()

            val t = (index * itemHeightPx + offset) / middle
            val textScaleY = 0f.coerceAtLeast(1f - abs(t) / 2f)
            val textRotationX = -t * rows
            val textColorAlpha = 0.25f.coerceAtLeast(1f - abs(t) / 1.5f)
            Label(
                text = labelAsString(text),
                itemHeight = itemHeight,
                offsetY = translateY.roundToInt(),
                textScaleY = textScaleY,
                textRotationX = textRotationX,
                textColorAlpha = textColorAlpha
            )
        }
        CenterLine(boxHeight = actualHeightPx)
    }
}

@Composable
private fun Label(
    text: String,
    itemHeight: Dp,
    offsetY: Int,
    textScaleY: Float = 1f,
    textRotationX: Float = 0f,
    textColorAlpha: Float = 1f
) {
    Box(
        modifier = Modifier
            .height(itemHeight)
            .fillMaxWidth()
            .offset { IntOffset(x = 0, y = offsetY) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body1.copy(
                color = MaterialTheme.colors.primary.copy(
                    alpha = textColorAlpha
                )
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .graphicsLayer(
                    transformOrigin = TransformOrigin.Center,
                    scaleY = textScaleY,
                    rotationX = textRotationX
                )
                .pointerInput(Unit) {
                    detectTapGestures(onLongPress = {
                        // disable text selection
                    })
                }
        )
    }
}

@Composable
private fun CenterLine(
    modifier: Modifier = Modifier,
    boxHeight: Float,
    forceDraw: Boolean = false
) {
    if (forceDraw) {
        Canvas(modifier = modifier.fillMaxSize()) {
            drawLine(
                color = Color.Red,
                start = Offset(0f, boxHeight / 2f),
                end = Offset(size.width, boxHeight / 2f),
                strokeWidth = 1f
            )
        }
    }
}


// todo: this method, possibly, is a culprit of overscroll bug
private suspend fun Animatable<Float, AnimationVector1D>.fling(
    initialVelocity: Float,
    lowerBound: Float = 0f,
    upperBound: Float,
    itemHeight: Float
): AnimationResult<Float, AnimationVector1D> {
    val targetValue =
        exponentialDecay<Float>(frictionMultiplier = 1f).calculateTargetValue(
            value,
            initialVelocity
        )
    val target = when {
        targetValue > lowerBound -> itemHeight / 2f
        abs(targetValue) >= upperBound + itemHeight -> -(upperBound)
        else -> targetValue
    }
    val coercedTarget = target % itemHeight
    val coercedAnchors =
        listOf(-itemHeight, 0f, itemHeight)
    val coercedPoint =
        coercedAnchors.minByOrNull { abs(it - coercedTarget) }!!
    val base = itemHeight * (target / itemHeight).toInt()
    val adjustTarget = coercedPoint + base
    return animateTo(
        targetValue = adjustTarget,
        animationSpec = spring(dampingRatio = 1f, stiffness = Spring.StiffnessLow),
        initialVelocity = initialVelocity
    )
}
