package ru.vt.core.ui.compose.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

@Composable
@ReadOnlyComposable
fun pxToDp(px: Int): Dp = with(density()) { px.toDp() }

@Composable
@ReadOnlyComposable
fun pxToDp(px: Float): Dp = with(density()) { px.toDp() }

@Composable
@ReadOnlyComposable
fun dpToPx(dp: Dp): Float = with(density()) { dp.toPx() }

@Composable
@ReadOnlyComposable
fun spToPx(sp: TextUnit): Float = with(density()) { sp.toPx() }

@Composable
@ReadOnlyComposable
private fun density(): Density = LocalDensity.current