package com.fingerprintjs.android.fingerprint.fingerprinters


abstract class Fingerprinter(
    val version: Int
) {
    abstract fun calculate(): String
}