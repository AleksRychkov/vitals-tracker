package ru.vt.presentation.dashboard.main.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.vt.core.resources.R
import ru.vt.core.ui.compose.components.AvatarComposable
import ru.vt.core.ui.compose.components.ExpandBottomSheet
import ru.vt.core.ui.compose.utils.textResource
import ru.vt.core.ui.compose.theme.Medium
import ru.vt.core.ui.compose.theme.Normal
import ru.vt.core.ui.compose.theme.Normal2X
import ru.vt.presentation.dashboard.main.StateDashboard


@Composable
internal fun Header(
    state: StateDashboard,
    expandBottomSheet: ExpandBottomSheet,
    editTextPosition: MutableState<Offset?>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(start = Normal2X),
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.primary,
            textAlign = TextAlign.Center,
            text = textResource(id = R.string.screen_dashboard_summary)
        )

        Box(modifier = Modifier
            .padding(Medium)
            .clickable {
                expandBottomSheet()
            }
        ) {
            Text(
                modifier = Modifier
                    .padding(Medium)
                    .onGloballyPositioned {
                        if (editTextPosition.value == null) {
                            val rect = it.boundsInRoot()
                            editTextPosition.value = rect.bottomCenter
                        }
                    },
                style = MaterialTheme.typography.subtitle1.copy(color = MaterialTheme.colors.primaryVariant),
                textAlign = TextAlign.Center,
                text = textResource(id = R.string.edit).lowercase()
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        AvatarComposable(
            letter = (state.profileName?.first() ?: '?').toString(),
            modifier = Modifier
                .fillMaxHeight()
                .padding(Normal)
                .aspectRatio(1f)
                .clipToBounds(),
            onClick = {

            }
        )
    }
}