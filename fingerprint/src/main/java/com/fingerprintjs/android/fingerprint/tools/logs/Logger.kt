package com.fingerprintjs.android.fingerprint.tools.logs

import android.util.Log

internal object Logger {

    fun e(msg: String = "", throwable: Throwable? = null) {
        if (throwable == null) {
            Log.e(LOG_TAG, msg)
        } else {
            Log.e(LOG_TAG, msg, throwable)
        }
    }

    private const val LOG_TAG = "FingerprintJS"
}
