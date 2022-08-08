package ru.vt.core.common.extension

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import ru.vt.core.common.event.EventHandler

fun <T> Flow<T>.flowOnIO(): Flow<T> = this.flowOn(Dispatchers.IO)
fun <T> Flow<T>.flowOnDefault(): Flow<T> = this.flowOn(Dispatchers.Default)

fun <T> Flow<Result<T>>.catchError() = this.catch { exception ->
    emit(Result.failure(exception))
}

fun <T> Flow<Result<T>>.handle(
    onSuccess: suspend (T) -> Unit = { },
    onError: ((Throwable, Boolean) -> Unit)? = null,
    handleError: (Throwable) -> Boolean = { _ -> false },
    eventHandler: EventHandler? = null,
    dispatcher: CoroutineDispatcher = Dispatchers.Default
): Flow<Result<T>> = this
    .onEach { result ->
        if (result.isSuccess) {
            onSuccess(requireNotNull(result.getOrNull()))
        } else if (result.isFailure) {
            val throwable = requireNotNull(result.exceptionOrNull())
            val errorHandled = handleError(throwable)
            if (errorHandled.not()) {
                eventHandler?.handleError(throwable)
            }
            onError?.invoke(throwable, errorHandled)
        }
    }
    .flowOn(dispatcher)

fun <T> Flow<Result<T?>>.handleNullable(
    onSuccess: suspend  (T?) -> Unit = { },
    onError: ((Throwable, Boolean) -> Unit)? = null,
    handleError: (Throwable) -> Boolean = { _ -> false },
    eventHandler: EventHandler? = null,
    dispatcher: CoroutineDispatcher = Dispatchers.Default
): Flow<Result<T?>> = this
    .onEach { result ->
        if (result.isSuccess) {
            onSuccess(result.getOrNull())
        } else if (result.isFailure) {
            val throwable = requireNotNull(result.exceptionOrNull())
            val errorHandled = handleError(throwable)
            if (errorHandled.not()) {
                eventHandler?.handleError(throwable)
            }
            onError?.invoke(throwable, errorHandled)
        }
    }
    .flowOn(dispatcher)
