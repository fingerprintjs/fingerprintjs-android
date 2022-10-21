package com.fingerprintjs.android.fingerprint.tools

import android.annotation.SuppressLint
import com.fingerprintjs.android.fingerprint.IdentificationVersion

@SuppressLint("DiscouragedApi")
internal fun IdentificationVersion.inRange(
    added: IdentificationVersion,
    removed: IdentificationVersion?,
): Boolean {
    return this.intValue >= added.intValue &&
            (if (removed != null) this.intValue < removed.intValue else true)
}
