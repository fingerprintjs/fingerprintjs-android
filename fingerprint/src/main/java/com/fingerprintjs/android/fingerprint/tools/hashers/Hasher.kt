package com.fingerprintjs.android.fingerprint.tools.hashers


/**
 * An interface for a hashing function used [Fingerprinter][com.fingerprintjs.android.fingerprint.Fingerprinter].
 */
public interface Hasher {
    public fun hash(data: String): String
}