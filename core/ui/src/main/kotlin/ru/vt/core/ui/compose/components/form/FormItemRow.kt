package ru.vt.core.ui.compose.components.form

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import ru.vt.core.ui.compose.utils.textResource
import ru.vt.core.ui.compose.theme.Normal
import ru.vt.core.ui.compose.utils.noRippleClickable

@Composable
inline fun FormItemRow(
    focusManager: FocusManager,
    @StringRes title: Int = 0,
    annotatedTitle: AnnotatedString? = null,
    crossinline onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(Normal)
            .noRippleClickable {
                focusManager.clearFocus(true)
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (annotatedTitle == null) {
            Text(
                modifier = Modifier,
                text = textResource(id = title),
                color = MaterialTheme.colors.secondaryVariant,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle1
            )
        } else {
            Text(
                modifier = Modifier,
                text = annotatedTitle,
                color = MaterialTheme.colors.secondaryVariant,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle1
            )
        }

        content()
    }
}