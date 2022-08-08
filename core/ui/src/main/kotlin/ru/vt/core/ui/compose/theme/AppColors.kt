package ru.vt.core.ui.compose.theme

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


val primaryVariant = Color(0xFF0A73FE)
val secondary = Color(0xFFEC6330)
val secondaryVariant = Color(0xFF808080)

val error = Color(0xFFFA3031)

val AppLightColors = lightColors(
    primary = Color.Black,
    primaryVariant = primaryVariant,
    onPrimary = Color.White,
    secondary = secondary,
    onSecondary = Color.White,
    secondaryVariant = secondaryVariant,
    onSurface = Color.Black,
    onBackground = Color.Black,
    error = error,
    onError = Color.White,
    surface = Color.White,
    background = Color(0xFFF2F1F6)
)

val AppDarkColors = darkColors(
    primary = Color.White,
    primaryVariant = primaryVariant,
    onPrimary = Color.Black,
    secondary = secondary,
    onSecondary = Color.White,
    secondaryVariant = secondaryVariant,
    onSurface = Color.White,
    onBackground = Color.White,
    error = error,
    onError = Color.White,
    surface = Color(0xFF2C2C2E),
    background = Color(0xFF1C1C1E)
)

@get:Composable
val Colors.divider: Color
    get() = if (isLight) Color(0xFFE2E2E2) else Color(0xFF323232)

@get:Composable
val Colors.bloodPressure: Color
    get() = if (isLight) Color(0xFFFB0E55) else Color(0xFFFC205F)

@get:Composable
val Colors.tabBar: Color
    get() = if (isLight) Color(0xFFEEEEEE) else Color(0xFF1C1B20)

@get:Composable
val Colors.tabBarSelector: Color
    get() = if (isLight) Color(0xFFFFFFFF) else Color(0xFF5B5A60)

@get:Composable
val Colors.measurableBG: Color
    get() = if (isLight) Color.White else Color.Black

@get:Composable
val Colors.headaches: Color
    get() = if (isLight) Color(0xFFB71C1C) else Color(0xFFEA5247)

@get:Composable
val Colors.weight: Color
    get() = if (isLight) Color(0xFF0085FF) else Color(0xFF168FFF)

@get:Composable
val Colors.chips: Color
    get() = if (isLight) Color(0xFFF2F1F6) else Color(0xFF1C1C1E)