package ru.vt.core.ui.viewmodel

import android.os.Parcelable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.vt.core.common.ResourceManger
import ru.vt.core.common.event.EventHandler
import ru.vt.core.common.extension.handle
import timber.log.Timber

abstract class LightViewModel<State : Parcelable, Action>(
    protected val coroutineScope: CoroutineScope,
    protected val eventHandler: EventHandler?,
    protected val resourceManger: ResourceManger
) {
    private val _actions = MutableSharedFlow<Action>()
    private val _stateFlow: MutableStateFlow<State?> = MutableStateFlow(null)

    val stateFlow: Flow<State?>
        get() = _stateFlow

    protected val state: State?
        get() = _stateFlow.value

    protected fun render(newState: State) {
        Timber.d("[LightViewModel] render: $newState")
        _stateFlow.value = newState
    }

    fun submitAction(action: Action) {
        Timber.d("[LightViewModel] submitAction: $action")
        coroutineScope.launch {
            _actions.emit(action)
        }
    }

    protected open fun handleError(t: Throwable): Boolean {
        return false
    }

    protected fun <T> Flow<Result<T>>.handle(onSuccess: (T) -> Unit = { }): Flow<Result<T>> =
        this.handle(
            onSuccess = onSuccess,
            handleError = ::handleError,
            eventHandler = eventHandler
        )

    protected open suspend fun processAction(action: Action) {}

    init {
        coroutineScope.launch {
            _actions.collect { value ->
                processAction(value)
            }
        }
    }
}
