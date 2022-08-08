package ru.vt.core.ui.compose.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.LocalWindowInsets
import ru.vt.domain.common.AppUnits

fun Modifier.pickerBottomSheetWithPaddings(
    enabled: Boolean,
    paddingBottom: Dp
): Modifier = this.padding(bottom = if (enabled) paddingBottom else 0.dp)

inline fun Modifier.noRippleClickableConditioned(
    condition: State<Boolean>,
    crossinline onClick: () -> Unit
): Modifier = composed {
    if (condition.value) {
        clickable(indication = null,
            interactionSource = remember { MutableInteractionSource() }) {
            onClick()
        }
    } else {
        this
    }
}

inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier = composed {
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

fun Modifier.maybeAddFocusRequester(focusRequester: FocusRequester?): Modifier = this.apply {
    focusRequester?.let {
        this.focusRequester(it)
    }
}

fun Modifier.clearFocusOnKeyboardDismiss(
    focusManager: FocusManager
): Modifier = composed {
    var isFocused by remember { mutableStateOf(false) }
    var keyboardAppearedSinceLastFocused by remember { mutableStateOf(false) }
    if (isFocused) {
        val imeIsVisible = LocalWindowInsets.current.ime.isVisible
        LaunchedEffect(imeIsVisible) {
            if (imeIsVisible) {
                keyboardAppearedSinceLastFocused = true
            } else if (keyboardAppearedSinceLastFocused) {
                focusManager.clearFocus()
            }
        }
    }
    onFocusEvent {
        if (isFocused != it.isFocused) {
            isFocused = it.isFocused
            if (isFocused) {
                keyboardAppearedSinceLastFocused = false
            }
        }
    }
}

@Composable
fun LazyListState.OnLoadMore(
    buffer: Int = 0,
    onLoadMore: () -> Unit
) {
    require(buffer >= 0) { "buffer cannot be negative, but was $buffer" }

    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem =
                layoutInfo.visibleItemsInfo.lastOrNull() ?: return@derivedStateOf true
            lastVisibleItem.index >= layoutInfo.totalItemsCount - 1 - buffer
        }
    }

    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }.collect { if (it) onLoadMore() }
    }
}

@Composable
fun Int?.convertWeightToString(): String {
    if (this == null || this == 0) return ""
    var txt = "${this / 1000}${textResource(key = AppUnits.KG.key)}"
    if (this % 1000 != 0) {
        txt += " ${this % 1000}${textResource(key = AppUnits.GRAMS.key)}"
    }
    return txt
}
