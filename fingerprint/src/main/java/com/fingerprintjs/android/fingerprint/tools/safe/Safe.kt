package com.fingerprintjs.android.fingerprint.tools.safe

import androidx.annotation.VisibleForTesting
import java.util.concurrent.Callable
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import java.util.concurrent.atomic.AtomicReference

internal object Safe {
    const val timeoutShort = 1_000L
    const val timeoutLong = 3_000L

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal var executor = createThreadPoolExecutor()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun createThreadPoolExecutor(): ThreadPoolExecutor =
        // defaults from Executors.newCachedThreadPool()
        ThreadPoolExecutor(
            0, Integer.MAX_VALUE,
            60L, TimeUnit.SECONDS,
            SynchronousQueue()
        )
}

/**
 * Runs the [block], catching all exceptions and handling unexpected execution locks (configured with [timeoutMs]).
 */
internal fun <T> safe(
    timeoutMs: Long = Safe.timeoutShort,
    block: () -> T,
): Result<T> {
    // we can't make a local variable volatile, hence using atomic reference here
    val executionThread = AtomicReference<Thread>(null)

    val future = runCatching {
        Safe.executor.submit(
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
    // using long timeout here because this function is used at the highest level and
    // may contain multiple safe calls inside
    timeoutMs: Long = Safe.timeoutLong,
    onError: (Throwable) -> Unit = {},
    block: () -> Unit,
) {
    runCatching {
        Safe.executor.execute {
            safe(timeoutMs = timeoutMs) { block() }
                .onFailure(onError)
        }
    }
        .onFailure(onError)
}

internal fun <T> safeLazy(
    timeoutMs: Long = Safe.timeoutShort,
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
