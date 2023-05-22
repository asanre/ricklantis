package io.asanre.app.core.domain

import kotlin.coroutines.cancellation.CancellationException

inline fun <R> Try(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
}

inline fun <R, T> Result<T>.mapResult(transform: (value: T) -> R): Result<R> {
    return fold({
        Try { transform(it) }
    }, {
        Result.failure(it)
    })
}

inline fun <T, R> Result<T>.flatMap(transform: (value: T) -> Result<R>) = fold({
    transform(it)
}, {
    Result.failure(it)
})
