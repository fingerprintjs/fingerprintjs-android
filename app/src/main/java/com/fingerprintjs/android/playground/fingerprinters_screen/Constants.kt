package com.fingerprintjs.android.playground.fingerprinters_screen

import com.fingerprintjs.android.fingerprint.signal_providers.SignalProviderType


const val DEFAULT_FINGERPRINTER_VERSION = 2
val DEFAULT_FINGERPRINT_MASK = (SignalProviderType.HARDWARE or SignalProviderType.OS_BUILD or SignalProviderType.DEVICE_STATE)