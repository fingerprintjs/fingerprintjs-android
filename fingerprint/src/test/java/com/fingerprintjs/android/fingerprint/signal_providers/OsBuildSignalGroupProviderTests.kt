package com.fingerprintjs.android.fingerprint.signal_providers

import com.fingerprintjs.android.fingerprint.info_providers.OsBuildInfoProvider
import com.fingerprintjs.android.fingerprint.signal_providers.os_build.OsBuildSignalGroupProvider
import com.fingerprintjs.android.fingerprint.tools.hashers.EmptyHasher
import junit.framework.Assert.assertEquals
import org.junit.Test

class OsBuildSignalGroupProviderTests {
    @Test
    fun `OsBuildFingerprinter v1 - success`() {
        val osBuildInfoProvider = object :
            OsBuildInfoProvider {
            override fun modelName() = "model"
            override fun manufacturerName() = "manufacturer"
            override fun androidVersion(): String {
                TODO("Not yet implemented")
            }

            override fun sdkVersion(): String {
                TODO("Not yet implemented")
            }

            override fun kernelVersion(): String {
                TODO("Not yet implemented")
            }

            override fun fingerprint() = "fingerprint"
        }

        val fingerprinter =
            OsBuildSignalGroupProvider(
                osBuildInfoProvider,
                null,
                EmptyHasher(),
                1
            )

        assertEquals("fingerprint", fingerprinter.fingerprint())
    }
}