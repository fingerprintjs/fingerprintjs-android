package com.fingerprintjs.android.fingerprint.tools.hashers

import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages

@Deprecated(message = DeprecationMessages.UTIL_UNINTENDED_PUBLIC_API)
public class EmptyHasher : Hasher {
    override fun hash(data: String): String {
        return data
    }
}