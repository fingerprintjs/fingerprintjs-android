package com.fingerprintjs.android.fingerprint.signal_providers


import com.fingerprintjs.android.fingerprint.info_providers.CodecInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.DeviceSecurityInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.MediaCodecInfo
import com.fingerprintjs.android.fingerprint.info_providers.OsBuildInfoProvider
import com.fingerprintjs.android.fingerprint.signal_providers.os_build.OsBuildSignalGroupProvider
import com.fingerprintjs.android.fingerprint.tools.hashers.EmptyHasher
import junit.framework.Assert.assertEquals
import org.junit.Test


class OsBuildSignalGroupProviderTests {
    @Test
    fun `OsBuildFingerprinter v1 - success`() {
        val osBuildSignalGroupProvider = createOsBuildGroupSignalProvider(1)
        assertEquals("fingerprint", osBuildSignalGroupProvider.fingerprint())
    }

    @Test
    fun `OsBuildFingerprinter v2 - success`() {
        val osBuildSignalGroupProvider = createOsBuildGroupSignalProvider(2)
        assertEquals("11304.14inactiveBouncy castle1.2.1Sun security2.0.0FLACaudio3dHEVCaudio3d", osBuildSignalGroupProvider.fingerprint())
    }

    @Test
    fun `OsBuildFingerprinter v2 - success, codec info provider is null`() {
        val osBuildSignalGroupProvider = createOsBuildGroupSignalProvider(2)
        assertEquals("11304.14inactiveBouncy castle1.2.1Sun security2.0.0FLACaudio3dHEVCaudio3d", osBuildSignalGroupProvider.fingerprint())
    }

    private fun createOsBuildGroupSignalProvider(
        version: Int,
        codecInfoProviderIsNull: Boolean = false
    ): OsBuildSignalGroupProvider {
        val osBuildInfoProvider = object :
            OsBuildInfoProvider {
            override fun modelName() = "model"
            override fun manufacturerName() = "manufacturer"
            override fun androidVersion() = "11"

            override fun sdkVersion() = "30"

            override fun kernelVersion() = "4.14"

            override fun fingerprint() = "fingerprint"
        }

        val deviceSecurityInfoProvider = object :
            DeviceSecurityInfoProvider {
            override fun encryptionStatus() = "inactive"

            override fun securityProvidersData() =
                listOf(Pair("Bouncy castle", "1.2.1"), Pair("Sun security", "2.0.0"))

            override fun isPinSecurityEnabled() = true
        }

        val codecInfoProvider = object : CodecInfoProvider {
            override fun codecsList() = listOf(
                MediaCodecInfo("FLAC", listOf("audio", "3d")),
                MediaCodecInfo("HEVC", listOf("audio", "3d"))
            )
        }


        return OsBuildSignalGroupProvider(
            osBuildInfoProvider,
            if (codecInfoProviderIsNull) null else codecInfoProvider,
            deviceSecurityInfoProvider,
            EmptyHasher(),
            version
        )
    }
}