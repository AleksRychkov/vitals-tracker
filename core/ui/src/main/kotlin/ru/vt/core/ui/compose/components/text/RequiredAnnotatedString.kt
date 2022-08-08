package ru.vt.core.ui.compose.components.text

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import ru.vt.core.ui.compose.theme.AppTheme
import ru.vt.core.ui.compose.utils.textResource

@Composable
fun requiredAnnotatedString(
    @StringRes append: Int
): AnnotatedString {
    return buildAnnotatedString {
        withStyle(SpanStyle(color = MaterialTheme.colors.secondary)) {
            append("* ")
        }
        append(textResource(id = append))
    }
}

@Composable
fun requiredAnnotatedString(
    append: String
): AnnotatedString {
    return buildAnnotatedString {
        withStyle(SpanStyle(color = MaterialTheme.colors.secondary)) {
            append("* ")
        }
        append(append)
    }
}

@Preview("RequiredAnnotatedString preview", showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    AppTheme {
        Box {
            Text(text = requiredAnnotatedString(append = "Hello World!"))
        }
    }
}
