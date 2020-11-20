package com.fingerprintjs.android.fingerprint


import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher
import com.fingerprintjs.android.fingerprint.tools.hashers.MurMur3x64x128Hasher


data class Configuration @JvmOverloads constructor(
    val version: Int,
    val hasher: Hasher = MurMur3x64x128Hasher()
)