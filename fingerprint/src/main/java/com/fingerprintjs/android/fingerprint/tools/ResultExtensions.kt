package com.fingerprintjs.android.fingerprint.tools

internal fun <V> Result<Result<V>>.flatten(): Result<V> {
    return when (this.isSuccess) {
        true -> this.getOrThrow()
        false -> Result.failure(this.exceptionOrNull()!!)
    }
}
