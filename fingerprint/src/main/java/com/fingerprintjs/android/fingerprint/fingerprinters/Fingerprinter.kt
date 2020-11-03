package com.fingerprintjs.android.fingerprint.fingerprinters


abstract class Fingerprinter<T> (
    val version: Int
) {
    abstract fun calculate(): String
    abstract fun rawData(): T
}