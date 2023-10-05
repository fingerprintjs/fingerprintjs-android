package com.fingerprintjs.android.fingerprint.tools.threading.safe

import androidx.annotation.VisibleForTesting
import com.fingerprintjs.android.fingerprint.tools.logs.Logger
import com.fingerprintjs.android.fingerprint.tools.logs.ePleaseReport
import com.fingerprintjs.android.fingerprint.tools.threading.sharedExecutor
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import java.util.concurrent.atomic.AtomicReference
import kotlin.concurrent.getOrSet

internal object Safe {
    const val timeoutShort = 1_000L

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal val runningInsideSafeWithTimeout = ThreadLocal<Boolean>()

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

    if (Safe.runningInsideSafeWithTimeout.getOrSet { false }) {
        Safe.logIllegalSafeWithTimeoutUsage()
    }

    val future = runCatching {
        sharedExecutor.submit(
            Callable {
                Safe.runningInsideSafeWithTimeout.set(true)
                executionThread.set(Thread.currentThread())
                try {
                    block()
                } finally {
                    Safe.runningInsideSafeWithTimeout.remove()
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
