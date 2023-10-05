package com.fingerprintjs.android.fingerprint.tools.threading.safe

import com.fingerprintjs.android.fingerprint.tools.threading.sharedExecutor
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import java.util.concurrent.atomic.AtomicReference

internal object Safe {
    const val timeoutShort = 1_000L
    const val timeoutLong = 3_000L
    const val timeoutNop = 0L
}

/**
 * Runs the [block], catching all exceptions and handling unexpected execution locks (configured with [timeoutMs]).
 */
internal fun <T> safe(
    timeoutMs: Long = Safe.timeoutShort,
    block: () -> T,
): Result<T> {
    return when (timeoutMs) {
        Safe.timeoutNop -> runCatching { block() }
        else -> safeWithTimeout(timeoutMs, block)
    }
}

private fun <T> safeWithTimeout(
    timeoutMs: Long,
    block: () -> T,
): Result<T> {
    // we can't make a local variable volatile, hence using atomic reference here
    val executionThread = AtomicReference<Thread>(null)

    val future = runCatching {
        sharedExecutor.submit(
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
