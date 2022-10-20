package com.fingerprintjs.android.fingerprint


import com.fingerprintjs.android.fingerprint.tools.hashers.MurMur3x64x128Hasher
import org.junit.Test

import org.junit.Assert.assertEquals


class HashTests {
    // Base check of the implementation with http://murmurhash.shorelabs.com/
    @Test
    fun `murmur3 64 128 hash test`() {
        val stringInput1 = "AndroidDevelopmentIsGreatBut"
        val stringInput2 = "You should be okay with Activity Lifecycle"
        val hasher =
            MurMur3x64x128Hasher()
        assertEquals("6bb34d81f9326054ef413d0a9621e518", hasher.hash(stringInput1))
        assertEquals("577a312a8a9b4a14a3800c2677233fde", hasher.hash(stringInput2))
        assertEquals("d7e561fb98ef89fb5753c60c611a9ea5", hasher.hash(stringInput1 + stringInput2))
        assertEquals("00", hasher.hash(""))
    }
}