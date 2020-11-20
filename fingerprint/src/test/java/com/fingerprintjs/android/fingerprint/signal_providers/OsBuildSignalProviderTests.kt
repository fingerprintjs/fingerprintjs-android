package com.fingerprintjs.android.fingerprint.signal_providers

import com.fingerprintjs.android.fingerprint.datasources.OsBuildInfoProvider
import com.fingerprintjs.android.fingerprint.signal_providers.os_build.OsBuildSignalProvider
import com.fingerprintjs.android.fingerprint.tools.hashers.EmptyHasher
import junit.framework.Assert.assertEquals
import org.junit.Test

class OsBuildSignalProviderTests {
    @Test
    fun `OsBuildFingerprinter v1 - success`() {
        val osBuildInfoProvider = object : OsBuildInfoProvider {
            override fun modelName() = "model"
            override fun manufacturerName() = "manufacturer"
            override fun fingerprint() = "fingerprint"
        }

        val fingerprinter =
            OsBuildSignalProvider(
                osBuildInfoProvider,
                EmptyHasher(),
                1
            )

        assertEquals("fingerprint", fingerprinter.fingerprint())
    }
}