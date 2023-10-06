package com.fingerprintjs.android.fingerprint.tools.threading.safe

import androidx.annotation.VisibleForTesting
import com.fingerprintjs.android.fingerprint.tools.logs.Logger
import com.fingerprintjs.android.fingerprint.tools.logs.ePleaseReport
import com.fingerprintjs.android.fingerprint.tools.threading.sharedExecutor
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import java.util.concurrent.atomic.AtomicReference

internal object Safe {
    const val timeoutShort = 1_000L
    const val timeoutLong = 3_000L

    private val runningInsideSafeWithTimeout = ThreadLocal<Boolean>()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun markInsideSafeWithTimeout() {
        runningInsideSafeWithTimeout.set(true)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun clearInsideSafeWithTimeout() {
        runningInsideSafeWithTimeout.remove()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun getInsideSafeWithTimeout(): Boolean {
        return runningInsideSafeWithTimeout.get() ?: false
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun logIllegalSafeWithTimeoutUsage() {
        Logger.ePleaseReport(IllegalStateException())
    }
}

internal inline fun <T> safe(block: () -> T) =
    runCatching(block)

/**
 * Runs the [block], catching all exceptions and handling unexpected execution locks (configured with [timeoutMs]).
 */
internal fun <T> safeWithTimeout(
    timeoutMs: Long = Safe.timeoutShort,
    block: () -> T,
): Result<T> {
    // we can't make a local variable volatile, hence using atomic reference here
    val executionThread = AtomicReference<Thread>(null)

    if (Safe.getInsideSafeWithTimeout()) {
        Safe.logIllegalSafeWithTimeoutUsage()
    }

    val future = runCatching {
        sharedExecutor.submit(
            Callable {
                Safe.markInsideSafeWithTimeout()
                executionThread.set(Thread.currentThread())
                try {
                    block()
                } finally {
                    Safe.clearInsideSafeWithTimeout()
                }
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
