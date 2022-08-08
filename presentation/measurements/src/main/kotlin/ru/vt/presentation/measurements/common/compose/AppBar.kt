package ru.vt.presentation.measurements.common.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import ru.vt.core.ui.compose.utils.textResource
import ru.vt.core.ui.compose.theme.Normal
import ru.vt.presentation.measurements.R

@Composable
internal fun MeasurementAppBar(
    @StringRes title: Int,
    onAdd: () -> Unit,
    onGenerateReport: () -> Unit,
    onBackPressed: () -> Unit
) {
    val displayMenu = remember { mutableStateOf(false) }

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
                    text = textResource(id = title),
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
                IconButton(onClick = { displayMenu.value = !displayMenu.value }) {
                    Icon(Icons.Default.MoreVert, "")
                }

                // Creating a dropdown menu
                DropdownMenu(
                    expanded = displayMenu.value,
                    onDismissRequest = { displayMenu.value = false }
                ) {

                    DropdownMenuItem(onClick = {
                        onAdd()
                    }) {
                        Text(text = textResource(id = R.string.add))
                    }

                    DropdownMenuItem(onClick = { onGenerateReport() }) {
                        Text(text = textResource(id = R.string.generate_report))
                    }
                }
            }
        )
    }
}
