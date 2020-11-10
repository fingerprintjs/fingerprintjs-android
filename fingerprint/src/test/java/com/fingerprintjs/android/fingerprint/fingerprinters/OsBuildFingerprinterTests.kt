package com.fingerprintjs.android.fingerprint.fingerprinters

import com.fingerprintjs.android.fingerprint.datasources.OsBuildInfoProvider
import com.fingerprintjs.android.fingerprint.fingerprinters.os_build_fingerprint.OsBuildFingerprinter
import com.fingerprintjs.android.fingerprint.tools.hashers.EmptyHasher
import junit.framework.Assert.assertEquals
import org.junit.Test

class OsBuildFingerprinterTests {
    @Test
    fun `OsBuildFingerprinter v1 - success`() {
        val osBuildInfoProvider = object : OsBuildInfoProvider {
            override fun modelName() = "model"
            override fun manufacturerName() = "manufacturer"
            override fun fingerprint() = "fingerprint"
        }

        val fingerprinter = OsBuildFingerprinter(
            osBuildInfoProvider,
            EmptyHasher(),
            1
        )

        assertEquals("fingerprint", fingerprinter.calculate())
    }
}