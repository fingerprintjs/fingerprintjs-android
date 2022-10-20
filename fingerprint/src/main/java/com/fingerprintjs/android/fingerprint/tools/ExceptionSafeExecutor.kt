package com.fingerprintjs.android.fingerprint.tools

import java.lang.Exception


@Deprecated(message = DeprecationMessages.UTIL_UNINTENDED_PUBLIC_API)
public fun <T> executeSafe(code: () -> T, defaultValue: T): T {
    return try {
        code()
    } catch (exception: Exception) {
        defaultValue
    }
}