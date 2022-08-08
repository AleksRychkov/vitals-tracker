package ru.vt.core.ui.compose.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.vt.core.ui.compose.theme.Normal
import ru.vt.core.ui.compose.utils.noRippleClickable

typealias ExpandBottomSheet = () -> Unit
typealias CollapseBottomSheet = () -> Unit

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppBottomSheet(
    modifier: Modifier = Modifier,
    sheetContent: @Composable (ExpandBottomSheet, CollapseBottomSheet) -> Unit,
    mainContent: @Composable (ExpandBottomSheet, CollapseBottomSheet) -> Unit
) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Collapsed
        )
    )
    val expandWorkAround = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val toggleBottomSheet: () -> Unit = {
        coroutineScope.launch {
            if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                bottomSheetScaffoldState.bottomSheetState.collapse()
            } else {
                bottomSheetScaffoldState.bottomSheetState.expand()
            }
        }
    }
    val expandBottomSheet: ExpandBottomSheet = {
        expandWorkAround.value = true
        coroutineScope.launch {
            if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                bottomSheetScaffoldState.bottomSheetState.expand()
            }
        }
    }
    val collapseBottomSheet: CollapseBottomSheet = {
        expandWorkAround.value = false
        coroutineScope.launch {
            if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                bottomSheetScaffoldState.bottomSheetState.collapse()
            }
        }
    }

    BackHandler(enabled = bottomSheetScaffoldState.bottomSheetState.isExpanded) {
        toggleBottomSheet()
    }

    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = bottomSheetScaffoldState,
        sheetBackgroundColor = MaterialTheme.colors.background,
        sheetShape = RoundedCornerShape(topStart = Normal, topEnd = Normal),
        sheetContent = {
            if (expandWorkAround.value) {
                sheetContent(expandBottomSheet, collapseBottomSheet)
            } else {
                Box(Modifier.fillMaxWidth().height(60.dp))
            }
        },
        sheetPeekHeight = 0.dp,
        backgroundColor = Color.Transparent
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            mainContent(expandBottomSheet, collapseBottomSheet)

            val currentBottomSheetProgress = bottomSheetScaffoldState.currentFraction
            if (bottomSheetScaffoldState.bottomSheetState.isExpanded || currentBottomSheetProgress > 0f) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black.copy(alpha = currentBottomSheetProgress / 5f))
                    .noRippleClickable {
                        coroutineScope.launch {
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                        }
                    })
            }
        }
    }
}

private var prevFraction: Float = -1f

@OptIn(ExperimentalMaterialApi::class)
private val BottomSheetScaffoldState.currentFraction: Float
    get() {
        val fraction = bottomSheetState.progress.fraction
        val direction = bottomSheetState.direction
        val currentValue = bottomSheetState.currentValue
        val targetValue = bottomSheetState.targetValue
        if (fraction == 0f || fraction == 1f) {
            return when {
                prevFraction > 0.9f && targetValue == BottomSheetValue.Expanded -> 1f
                prevFraction < 0.1f && targetValue == BottomSheetValue.Collapsed -> 0f
                currentValue == BottomSheetValue.Collapsed -> 0f
                else -> 1f
            }
        }
        val res = if (direction == -1f) {
            fraction
        } else {
            1f - fraction
        }
        prevFraction = res
        return res
    }