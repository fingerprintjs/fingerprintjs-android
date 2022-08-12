package com.fingerprintjs.android.playground.utils

import java.util.concurrent.CountDownLatch


internal class CallbackToSyncActions<T> {
    val countDownLatch = CountDownLatch(1)
    @Volatile
    var value: T? = null

    fun emit(value: T) {
        this.value = value
        countDownLatch.countDown()
    }
}

@Suppress("UNCHECKED_CAST")
internal fun <T> callbackToSync(
    // enabling @BuilderInference will allow us not to specify type arguments explicitly, but
    // this is still experimental feature.
    // @BuilderInference
    block: CallbackToSyncActions<T>.() -> Unit
): T {
    val callbackActions = CallbackToSyncActions<T>()

    callbackActions.block()
    callbackActions.countDownLatch.await()

    return callbackActions.value as T // cast to remove unnecessary nullability
}
