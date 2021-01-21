package com.fingerprintjs.android.playground.fingerprinters_screen

import android.content.Context
import com.fingerprintjs.android.fingerprint.Configuration
import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.FingerprinterFactory
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher
import com.fingerprintjs.android.fingerprint.tools.hashers.MurMur3x64x128Hasher

class FingerprinterProvider(private val applicationContext: Context) {
    fun provideInstance(version: Int, hasher: Hasher = MurMur3x64x128Hasher()): Fingerprinter {
        return FingerprinterFactory.getInstance(
            applicationContext, Configuration(
                version, hasher
            )
        )
    }
}