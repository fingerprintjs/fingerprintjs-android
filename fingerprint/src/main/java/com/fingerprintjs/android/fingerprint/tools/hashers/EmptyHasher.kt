package com.fingerprintjs.android.fingerprint.hashers

class EmptyHasher : Hasher {
    override fun hash(data: String): String {
        return data
    }

    override fun hash(data: ByteArray): ByteArray {
        return data
    }
}