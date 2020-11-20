package com.fingerprintjs.android.fingerprint.tools.hashers


interface Hasher {
    fun hash(data: String): String
}