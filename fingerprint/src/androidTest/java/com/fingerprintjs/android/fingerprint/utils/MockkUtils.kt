package com.fingerprintjs.android.fingerprint.utils

internal fun mockkObjectSupported(): Boolean =
    (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P)
