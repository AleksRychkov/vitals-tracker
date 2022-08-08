package ru.vt.core.ui.viewmodel

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner

interface ViewModelFactory<out V : ViewModel> {
    fun create(handle: SavedStateHandle): V
}

class AppSavedStateViewModelFactory<out V : ViewModel>(
    private val viewModelFactory: ViewModelFactory<V>,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return viewModelFactory.create(handle) as T
    }
}

@Composable
inline fun <reified VM : ViewModel> withFactory(
    factory: ViewModelFactory<VM>,
    defaultArgs: Bundle? = null
) = AppSavedStateViewModelFactory(factory, LocalSavedStateRegistryOwner.current, defaultArgs)

