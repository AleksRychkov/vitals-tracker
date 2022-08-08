package ru.vt.presentation.measurements.common.compose

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.vt.core.ui.compose.theme.Normal

@Composable
internal fun MeasurementFloatingButton(
    boxScope: BoxScope,
    coroutineScope: CoroutineScope,
    listState: LazyListState,
    floatingBtnAdd: State<Boolean>,
    create: () -> Unit
) {
    with(boxScope) {
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(Normal),
            onClick = {
                if (floatingBtnAdd.value.not()) {
                    coroutineScope.launch { listState.scrollToItem(0) }
                } else {
                    create()
                }
            },
            backgroundColor = MaterialTheme.colors.primaryVariant,
            content = {
                Crossfade(targetState = floatingBtnAdd.value) { addEnabled ->
                    if (addEnabled) {
                        Icon(
                            painter = rememberVectorPainter(image = Icons.Filled.Add),
                            contentDescription = "Add record",
                            tint = Color.White
                        )
                    } else {
                        Icon(
                            painter = rememberVectorPainter(image = Icons.Filled.ArrowUpward),
                            contentDescription = "Scroll up",
                            tint = Color.White
                        )
                    }
                }
            }
        )
    }
}