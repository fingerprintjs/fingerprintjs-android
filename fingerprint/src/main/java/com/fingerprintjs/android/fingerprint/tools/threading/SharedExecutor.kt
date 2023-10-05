package com.fingerprintjs.android.fingerprint.tools.threading

import androidx.annotation.VisibleForTesting
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

internal var sharedExecutor = createSharedExecutor()
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE) set

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
internal fun createSharedExecutor() =
    // defaults from Executors.newCachedThreadPool()
    ThreadPoolExecutor(
        0, Integer.MAX_VALUE,
        60L, TimeUnit.SECONDS,
        SynchronousQueue()
    )
