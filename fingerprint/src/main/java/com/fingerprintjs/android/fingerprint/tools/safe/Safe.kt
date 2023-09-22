package com.fingerprintjs.android.fingerprint.tools.safe

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import java.util.concurrent.atomic.AtomicReference

private const val DEFAULT_CALL_AWAIT_INTERVAL_MS = 3_000L
private val executor = Executors.newCachedThreadPool()

/**
 * Runs the [block], catching all exceptions and handling unexpected execution locks (configured with [timeoutMs]).
 */
internal fun <T> safe(
    timeoutMs: Long = DEFAULT_CALL_AWAIT_INTERVAL_MS,
    block: () -> T,
): Result<T> {
    // we can't make a local variable volatile, hence using atomic reference here
    val executionThread = AtomicReference<Thread>(null)

    val future = runCatching {
        executor.submit(
            Callable {
                executionThread.set(Thread.currentThread())
                block()
            }
        )!!
    }.getOrElse { return Result.failure(it) }

    return runCatching {
        future.get(timeoutMs, TimeUnit.MILLISECONDS)
    }
        .recoverCatching { t ->
            if (t is TimeoutException) {
                throw ExecutionTimeoutException(
                    timeoutException = t,
                    executionThreadStackTrace = executionThread.get()?.stackTrace?.filterNotNull(),
                )
            } else {
                throw t
            }
        }
        .onFailure { runCatching { future.cancel(true) } }
}

internal fun safeAsync(
    timeoutMs: Long = DEFAULT_CALL_AWAIT_INTERVAL_MS,
    onError: (Throwable) -> Unit = {},
    block: () -> Unit,
) {
    runCatching {
        executor.execute {
            safe(timeoutMs = timeoutMs) { block() }
                .onFailure(onError)
        }
    }
        .onFailure(onError)
}

internal fun <T> safeLazy(
    timeoutMs: Long = DEFAULT_CALL_AWAIT_INTERVAL_MS,
    constructor: () -> T,
): SafeLazy<T> = SafeLazy(timeoutMs, constructor)

internal class SafeLazy<T>(
    timeoutMs: Long,
    constructor: () -> T,
) {
    val res: Result<T> by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        safe(timeoutMs = timeoutMs) { constructor.invoke() }
    }

    fun getOrThrow(): T = res.getOrThrow()
}
