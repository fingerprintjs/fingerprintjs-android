package com.fingerprintjs.android.fingerprint.tools

internal object DeprecationMessages {

    const val UNREACHABLE_SYMBOL_UNINTENDED_PUBLIC_API = "This symbol has historically (and unintentionally) " +
            "been public, even though Fingerprint API does not provide any way " +
            "to reach to it. We will remove public visibility for this symbol in future versions."

    const val UTIL_UNINTENDED_PUBLIC_API= "This symbol has historically " +
            "been public but is intended for internal use only. " +
            "We will remove public visibility for this symbol in future versions."

    const val DEPRECATED_SYMBOL = "This symbol is deprecated because it may be useful only when using deprecated " +
            "Fingerprinter.getFingerprint(stabilityLevel, listener) or Fingerprinter.getDeviceId(listener) methods." +
            " Whenever we remove those methods from the library, we will remove this symbol as well."
}
