package ru.vt.core.ui.store

import kotlinx.coroutines.flow.MutableStateFlow

internal class StateStore<S>(initialState: S) : Store<S> {

    override val stateFlow: MutableStateFlow<S> = MutableStateFlow(value = initialState)

    override fun process(state: S) {
        stateFlow.value = state
    }
}