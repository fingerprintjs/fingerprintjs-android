package com.fingerprintjs.android.playground.fingerprinters_screen

import com.fingerprintjs.android.fingerprint.signal_providers.SignalGroupProviderType


const val DEFAULT_FINGERPRINTER_VERSION = 2
val DEFAULT_FINGERPRINT_MASK = (SignalGroupProviderType.HARDWARE or SignalGroupProviderType.OS_BUILD or SignalGroupProviderType.DEVICE_STATE)