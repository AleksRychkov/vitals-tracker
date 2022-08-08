package ru.vt.core.ui.compose.utils

import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import ru.vt.core.resources.TextResources

@Composable
@ReadOnlyComposable
fun textResource(@StringRes id: Int): String = TextResources.getString(id, resources())

@Composable
@ReadOnlyComposable
fun textResource(key: String): String = TextResources.getString(key, resources())

@Composable
@ReadOnlyComposable
private fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}
