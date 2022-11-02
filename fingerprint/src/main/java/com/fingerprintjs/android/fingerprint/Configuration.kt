package com.fingerprintjs.android.fingerprint


import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher
import com.fingerprintjs.android.fingerprint.tools.hashers.MurMur3x64x128Hasher


/**
 * This class represents instance-wide [Fingerprinter] parameters
 * that are passed to [FingerprinterFactory.getInstance] method.
 *
 * @param version identification version. See [Fingerprinter.Version] for details.
 * @param hasher hash implementation. Check out [Hasher] for details.
 */
@Deprecated(message = DeprecationMessages.DEPRECATED_SYMBOL)
public data class Configuration @JvmOverloads constructor(
    val version: Int,
    val hasher: Hasher = MurMur3x64x128Hasher()
)
