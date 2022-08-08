package ru.vt.core.ui.compose

import androidx.compose.runtime.compositionLocalOf
import ru.vt.core.sysservice.VibratorService

val LocalVibratorService = compositionLocalOf<VibratorService> {
    error("VibratorService not provided")
}
