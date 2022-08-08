package ru.vt.presentation.measurements.common.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import ru.vt.core.resources.R
import ru.vt.core.ui.compose.utils.textResource
import ru.vt.core.ui.compose.theme.Large
import ru.vt.core.ui.compose.theme.Normal

@Composable
fun CreateMeasurementHeader(
    title: String,
    onBackPressed: () -> Unit,
    onAddPressed: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .background(color = MaterialTheme.colors.surface),
        elevation = AppBarDefaults.TopAppBarElevation
    ) {
        TopAppBar(
            modifier = Modifier.statusBarsPadding(),
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h6.copy(color = MaterialTheme.colors.primary),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            backgroundColor = MaterialTheme.colors.surface,
            navigationIcon = {
                Icon(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .clickable {
                            onBackPressed()
                        }
                        .padding(Normal),
                    painter = rememberVectorPainter(Icons.Filled.ArrowBack),
                    contentDescription = null,
                    tint = MaterialTheme.colors.primaryVariant,
                )
            },
            elevation = 0.dp,
            actions = {
                Box(modifier = Modifier
                    .fillMaxHeight()
                    .width(IntrinsicSize.Max)
                    .clip(CircleShape)
                    .clickable {
                        focusManager.clearFocus(true)
                        onAddPressed()
                    }
                    .padding(Large),
                    contentAlignment = Alignment.Center
                )
                {
                    Text(
                        text = textResource(id = R.string.save).uppercase(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.subtitle1.copy(color = MaterialTheme.colors.primaryVariant)
                    )
                }
            }
        )
    }
}