package ru.vt.core.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.vt.core.common.extension.catchError
import ru.vt.core.common.extension.flowOnIO

abstract class UseCase<in Param, Res> {
    operator fun invoke(params: Param): Flow<Result<Res>> =
        flow { emit(execute(params)) }.flowOnIO().catchError()

    protected abstract suspend fun execute(params: Param): Result<Res>

    suspend fun executeSync(params: Param): Result<Res> = execute(params)

}
