package com.fingerprintjs.android.fingerprint.tools.hashers

class EmptyHasher : Hasher {
    override fun hash(data: String): String {
        return data
    }
}