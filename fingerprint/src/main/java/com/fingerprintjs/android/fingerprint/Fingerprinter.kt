package com.fingerprintjs.android.fingerprint


import com.fingerprintjs.android.fingerprint.device_id_providers.DeviceIdProvider
import com.fingerprintjs.android.fingerprint.fingerprinters.device_state.DeviceStateFingerprinter
import com.fingerprintjs.android.fingerprint.fingerprinters.hardware.HardwareFingerprinter
import com.fingerprintjs.android.fingerprint.fingerprinters.installed_apps.InstalledAppsFingerprinter
import com.fingerprintjs.android.fingerprint.fingerprinters.os_build_fingerprint.OsBuildFingerprinter


interface Fingerprinter {
    fun deviceId(): String

    fun fingerprint(): String

    fun fingerprint(flags: Int): String

    fun deviceIdProvider(): DeviceIdProvider

    fun hardwareFingerprinter(): HardwareFingerprinter

    fun osBuildFingerprinter(): OsBuildFingerprinter

    fun installedAppsFingerprinter(): InstalledAppsFingerprinter

    fun deviceStateFingerprinter(): DeviceStateFingerprinter
}

object Type {
    @JvmField
    val HARDWARE = 1

    @JvmField
    val OS_BUILD = 1 shl 1

    @JvmField
    val INSTALLED_APPS = 1 shl 2

    @JvmField
    val DEVICE_STATE = 1 shl 3
}



