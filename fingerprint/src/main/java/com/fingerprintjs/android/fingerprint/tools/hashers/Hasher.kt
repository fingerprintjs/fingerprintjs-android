package com.fingerprintjs.android.fingerprint.tools.hashers


interface Hasher {
    fun hash(data: String): String
    fun hash(data: ByteArray): ByteArray
}