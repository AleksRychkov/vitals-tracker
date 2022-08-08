package ru.vt.core.ui.viewmodel

import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.VisibleForTesting
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import ru.vt.core.common.ResourceManger
import ru.vt.core.common.event.EventHandler
import ru.vt.core.common.extension.handle
import ru.vt.core.common.extension.lazyNone
import ru.vt.core.navigation.contract.Navigator
import ru.vt.core.ui.store.StateStore
import ru.vt.core.ui.store.Store
import timber.log.Timber

typealias TestErrorHook = (Throwable, Boolean) -> Unit

abstract class BaseViewModel<State : Parcelable, Action>(
    private val savedStateHandle: SavedStateHandle,
    protected val eventHandler: EventHandler?,
    protected val resourceManger: ResourceManger,
    protected val navigator: Navigator
) : ViewModel() {

    private val key: String
        get() = javaClass.name

    protected abstract val initialState: State

    private val pendingActions = MutableSharedFlow<Action>()
    private val stateStore: Store<State> by lazyNone {
        StateStore(initialState()).apply { configureSavedStateProvider() }
    }

    val stateFlow: Flow<State>
        get() = stateStore.stateFlow

    fun initialState(): State = restoreState() ?: initialState
    fun hasRestoredState(): Boolean = restoreState() != null

    protected val state: State
        get() = (stateStore.stateFlow as StateFlow).value

    private fun configureSavedStateProvider() {
        savedStateHandle.setSavedStateProvider(key) {
            bundleOf("state" to state)
        }
    }

    private fun restoreState(): State? = with(savedStateHandle.get<Bundle>(key)) {
        @Suppress("UNCHECKED_CAST")
        this?.get("state") as? State
    }


    protected fun render(newState: State) {
        Timber.d("[BaseViewModel] render: $newState")
        stateStore.process(newState)
    }

    fun submitAction(action: Action) {
        Timber.d("[BaseViewModel] submitAction: $action")
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }

    protected open suspend fun processAction(action: Action) {}

    /**
     * return true if error handled and shouldn't be propagated to EventHandler
     */
    protected open fun handleError(t: Throwable): Boolean {
        return false
    }

    /**
     * Note: handle is running on background thread
     */
    protected fun <T> Flow<Result<T>>.handle(onSuccess: suspend (T) -> Unit = { }): Flow<Result<T>> =
        this.handle(
            onSuccess = onSuccess,
            onError = testErrorHook,
            handleError = ::handleError,
            eventHandler = eventHandler
        )

    init {
        viewModelScope.launch {
            pendingActions.collect { value ->
                processAction(value)
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var testErrorHook: TestErrorHook? = null
}
