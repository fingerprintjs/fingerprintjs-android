package com.fingerprintjs.android.fingerprint.tools

import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

private val executor = Executors.newCachedThreadPool()

internal fun <T> await(
    timeoutMillis: Long,
    block: () -> T
): Result<T> {
    val countDownLatch = CountDownLatch(1)
    var res: T? = null

    val future = try {
        executor.submit {
            res = block.invoke()
            countDownLatch.countDown()
        }
    } catch (e: RejectedExecutionException) {
        return Result.failure(e)
    }

    val completed = countDownLatch.await(timeoutMillis, TimeUnit.MILLISECONDS)
    if (completed) {
        @Suppress("UNCHECKED_CAST")
        return Result.success(res as T)
    } else {
        future.cancel(true)
        return Result.failure(TimeoutException())
    }
}
