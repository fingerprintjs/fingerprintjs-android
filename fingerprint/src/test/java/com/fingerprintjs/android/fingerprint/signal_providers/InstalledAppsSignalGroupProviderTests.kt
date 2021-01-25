package com.fingerprintjs.android.fingerprint.signal_providers

import com.fingerprintjs.android.fingerprint.info_providers.PackageInfo
import com.fingerprintjs.android.fingerprint.info_providers.PackageManagerDataSource
import com.fingerprintjs.android.fingerprint.signal_providers.installed_apps.InstalledAppsSignalGroupProvider
import com.fingerprintjs.android.fingerprint.tools.hashers.EmptyHasher
import junit.framework.Assert.assertEquals
import org.junit.Test


class InstalledAppsSignalGroupProviderTests {

    @Test
    fun `InstalledAppsFingerprinter v1 - success`() {
        val packageManagerDataSource = object :
            PackageManagerDataSource {
            override fun getApplicationsList() = listOf(
                PackageInfo(
                    "app3"
                ),
                PackageInfo(
                    "app2"
                ),
                PackageInfo(
                    "app4"
                ),
                PackageInfo(
                    "app1"
                )
            )

            override fun getSystemApplicationsList() = listOf(
                PackageInfo(
                    "app3"
                ),
                PackageInfo(
                    "app2"
                )
            )
        }

        val fingerprinter =
            InstalledAppsSignalGroupProvider(
                packageManagerDataSource,
                EmptyHasher(),
                1
            )

        assertEquals("app1app2app3app4", fingerprinter.fingerprint(StabilityLevel.UNIQUE))
    }

    @Test
    fun `InstalledAppsFingerprinter v2 - success`() {
        val packageManagerDataSource = object :
            PackageManagerDataSource {
            override fun getApplicationsList() = listOf(
                PackageInfo(
                    "app3"
                ),
                PackageInfo(
                    "app2"
                ),
                PackageInfo(
                    "app4"
                ),
                PackageInfo(
                    "app1"
                )
            )

            override fun getSystemApplicationsList() = listOf(
                PackageInfo(
                    "app3"
                ),
                PackageInfo(
                    "app2"
                )
            )
        }

        val fingerprinter =
            InstalledAppsSignalGroupProvider(
                packageManagerDataSource,
                EmptyHasher(),
                2
            )

        assertEquals("app1app2app3app4app2app3", fingerprinter.fingerprint(StabilityLevel.UNIQUE))
        assertEquals("app2app3", fingerprinter.fingerprint(StabilityLevel.OPTIMAL))
    }
}