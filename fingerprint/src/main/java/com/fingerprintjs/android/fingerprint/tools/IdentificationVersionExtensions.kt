package com.fingerprintjs.android.fingerprint.tools

import android.annotation.SuppressLint
import com.fingerprintjs.android.fingerprint.Fingerprinter

@SuppressLint("DiscouragedApi")
internal fun Fingerprinter.Version.inRange(
    added: Fingerprinter.Version,
    removed: Fingerprinter.Version?,
): Boolean {
    return this.intValue >= added.intValue &&
            (if (removed != null) this.intValue < removed.intValue else true)
}
