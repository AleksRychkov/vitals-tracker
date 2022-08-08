package ru.vt.core.ui.compose.components.picker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.vt.core.ui.compose.components.WheelComposable
import ru.vt.core.ui.compose.utils.textResource
import ru.vt.core.ui.compose.theme.Normal

@Composable
fun GenderPickerComposable(
    modifier: Modifier,
    current: String = "",
    genders: List<String>,
    onGenderSelected: (String) -> Unit
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(Normal)
    ) {
        val itemHeight: Dp = 30.dp

        WheelComposable(
            modifier = Modifier.fillMaxWidth(),
            value = current,
            itemHeight = itemHeight,
            data = genders,
            labelAsString = {
                textResource(key = this)
            },
            onItemSelected = onGenderSelected
        )
        PickerIndicator(boxScope = this, itemHeight = itemHeight)
    }
}