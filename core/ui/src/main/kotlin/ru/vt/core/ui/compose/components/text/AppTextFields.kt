package ru.vt.core.ui.compose.components.text

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import ru.vt.core.ui.compose.theme.AppTheme
import ru.vt.core.ui.compose.theme.Normal

@Composable
fun TextFieldAlignEndComposable(
    modifier: Modifier = Modifier,
    focusManager: FocusManager,
    focusRequester: FocusRequester,
    value: String?,
    onValueChange: (String) -> Unit,
    hint: String?,
    textColorInFocus: Color = MaterialTheme.colors.primaryVariant,
    textColorInRest: Color = MaterialTheme.colors.primary,
    hintColor: Color = MaterialTheme.colors.secondaryVariant,
    textStyle: TextStyle = MaterialTheme.typography.subtitle1.copy(
        color = textColorInFocus,
        textAlign = TextAlign.End
    ),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Done
    ),
    keyboardActions: KeyboardActions = KeyboardActions(
        onDone = { focusManager.clearFocus() },
        onNext = { focusManager.moveFocus(FocusDirection.Next) }
    ),
    onFocusChanged: (Boolean) -> Unit = { _ -> }
) {
    Box(
        modifier = modifier
            .height(IntrinsicSize.Max)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            val isFocused = remember { mutableStateOf(false) }
            val _textStlye =
                textStyle.copy(color = if (isFocused.value) textColorInFocus else textColorInRest)
            IgnoreTextToolbarWrapper {
                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester = focusRequester)
                        .onFocusChanged {
                            onFocusChanged(it.isFocused)
                            isFocused.value = it.isFocused
                        },
                    value = value ?: "",
                    onValueChange = onValueChange,
                    textStyle = _textStlye,
                    cursorBrush = SolidColor(textColorInFocus),
                    singleLine = true,
                    maxLines = 1,
                    keyboardActions = keyboardActions,
                    keyboardOptions = keyboardOptions
                )
            }
        }
        if (value.isNullOrEmpty() && hint.isNullOrEmpty().not()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = hint!!,
                    color = hintColor,
                    style = MaterialTheme.typography.subtitle2
                )
            }
        }
    }
}

@Preview("AppTextField Preview", showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    AppTheme {
        val focusManager = LocalFocusManager.current
        val focusRequester = FocusRequester()
        Box {
            Column {
                TextFieldAlignEndComposable(
                    modifier = Modifier
                        .padding(Normal)
                        .fillMaxWidth(),
                    focusManager = focusManager,
                    focusRequester = focusRequester,
                    value = "Long long editable text",
                    onValueChange = {},
                    hint = "Hint"
                )
                TextFieldAlignEndComposable(
                    modifier = Modifier
                        .padding(Normal)
                        .fillMaxWidth(),
                    focusManager = focusManager,
                    focusRequester = focusRequester,
                    value = "",
                    onValueChange = {},
                    hint = "Hint"
                )
            }
        }
    }
}