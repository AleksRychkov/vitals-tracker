package ru.vt.presentation.dashboard.main.ui

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsPadding
import ru.vt.core.resources.R
import ru.vt.core.resources.TextResources
import ru.vt.core.ui.compose.theme.AppTheme
import ru.vt.core.ui.compose.theme.Medium
import ru.vt.core.ui.compose.theme.Normal
import ru.vt.core.ui.compose.theme.divider
import ru.vt.domain.common.Measurement
import ru.vt.presentation.dashboard.main.ActionDashboard
import ru.vt.presentation.dashboard.main.StateDashboard
import ru.vt.presentation.dashboard.main.TrackedItem

@Composable
internal fun EditSummary(
    state: StateDashboard,
    actionHandler: (ActionDashboard) -> Unit
) {
    EditSummary(trackedItems = state.trackedItems, onClick = { type, isTracked ->
        actionHandler(ActionDashboard.UpdateTrackedMeasurement(type, isTracked))
    })
}

@Composable
private fun EditSummary(
    trackedItems: List<TrackedItem>,
    onClick: (Int, Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .navigationBarsPadding()
    ) {
        trackedItems.forEachIndexed { index, item ->
            Item(item = item, onClick = onClick)
            if (index != trackedItems.size - 1) {
                Divider(
                    modifier = Modifier.padding(start = Medium, end = Medium),
                    color = MaterialTheme.colors.divider
                )
            }
        }
    }
}

@Composable
private fun Item(item: TrackedItem, onClick: (Int, Boolean) -> Unit) {
    val drawable: Painter = when (item.type) {
        Measurement.HEADACHE -> rememberVectorPainter(image = Icons.Filled.Bolt)
        Measurement.WEIGHT -> painterResource(id = R.drawable.ic_weight_filled)
        Measurement.PRESSURE_AND_HEART_RATE -> rememberVectorPainter(image = Icons.Filled.Favorite)
        else -> rememberVectorPainter(image = Icons.Filled.MoreHoriz)
    }
    val name = TextResources.getNameOfMeasurement(
        type = item.type,
        resources = LocalContext.current.resources
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable {
                onClick(item.type, !item.isTracked)
            }
            .padding(Normal),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = drawable,
            contentDescription = name,
            tint = MaterialTheme.colors.primary
        )
        Text(
            modifier = Modifier
                .padding(start = Normal)
                .weight(1f, fill = true),
            text = name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.primary)
        )
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clip(CircleShape)
                .clipToBounds()
            ,
            contentAlignment = Alignment.Center
        ) {
            TrackedIcon(
                isVisible = item.isTracked,
                drawableRes = R.drawable.ic_star_filled
            )
            TrackedIcon(
                isVisible = item.isTracked.not(),
                drawableRes = R.drawable.ic_star_outlined
            )
        }
    }
}

@Composable
private fun TrackedIcon(isVisible: Boolean, @DrawableRes drawableRes: Int) {
    AnimatedVisibilityIsTrackedButton(isVisible = isVisible) {
        Icon(
            modifier = Modifier.size(24.dp, 24.dp),
            painter = painterResource(drawableRes),
            tint = MaterialTheme.colors.primaryVariant,
            contentDescription = null
        )
    }
}

@Composable
private fun AnimatedVisibilityIsTrackedButton(
    isVisible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut(),
        content = content
    )
}

@Preview("Preview", showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    AppTheme {
        EditSummary(
            trackedItems = listOf(
                TrackedItem(Measurement.PRESSURE_AND_HEART_RATE, false),
                TrackedItem(Measurement.WEIGHT, false),
                TrackedItem(Measurement.HEADACHE, false),
            )
        ) { _, _ -> }
    }
}