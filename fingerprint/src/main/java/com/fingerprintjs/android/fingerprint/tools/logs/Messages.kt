package com.fingerprintjs.android.fingerprint.tools.logs

internal fun Logger.ePleaseReport(throwable: Throwable?) {
    e(
        msg = "Unexpected error occurred. Feel free to create an issue on Github repository of the fingerprintjs-android library.",
        throwable = throwable,
    )
}
