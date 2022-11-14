package com.fingerprintjs.android.playground.utils.mappers

import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel

val Fingerprinter.Version.description: String
    get() = "V${this.ordinal + 1}"

val StabilityLevel.description: String
    get() = this.name.lowercase().replaceFirstChar { it.uppercase() }