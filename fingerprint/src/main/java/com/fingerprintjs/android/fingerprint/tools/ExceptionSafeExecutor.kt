package com.fingerprintjs.android.fingerprint.tools

import java.lang.Exception


fun <T> executeSafe(code: () -> T, defaultValue: T): T {
    return try {
        code()
    } catch (exception: Exception) {
        defaultValue
    }
}