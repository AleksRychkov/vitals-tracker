package ru.vt.core.ui.store

import kotlinx.coroutines.flow.Flow


internal interface Store<S> {
    val stateFlow: Flow<S>

    fun process(state: S)
}