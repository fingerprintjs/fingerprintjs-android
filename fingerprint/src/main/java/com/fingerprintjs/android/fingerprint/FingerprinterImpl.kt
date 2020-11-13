package com.fingerprintjs.android.fingerprint


import com.fingerprintjs.android.fingerprint.device_id_providers.DeviceIdProvider
import com.fingerprintjs.android.fingerprint.fingerprinters.BaseFingerprinter
import com.fingerprintjs.android.fingerprint.fingerprinters.device_state.DeviceStateFingerprinter
import com.fingerprintjs.android.fingerprint.fingerprinters.hardware.HardwareFingerprinter
import com.fingerprintjs.android.fingerprint.fingerprinters.installed_apps.InstalledAppsFingerprinter
import com.fingerprintjs.android.fingerprint.fingerprinters.os_build_fingerprint.OsBuildFingerprinter
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher
import java.util.LinkedList


class FingerprinterImpl(
    private val hardwareFingerprinter: HardwareFingerprinter,
    private val osBuildFingerprinter: OsBuildFingerprinter,
    private val deviceIdProvider: DeviceIdProvider,
    private val installedAppsFingerprinter: InstalledAppsFingerprinter,
    private val deviceStateFingerprinter: DeviceStateFingerprinter,
    private val hasher: Hasher
) : Fingerprinter {

    override fun deviceId() = deviceIdProvider.getDeviceId()

    override fun fingerprint(): String {
        return fingerprint(
            flags = (
                    Type.HARDWARE or
                            Type.OS_BUILD or
                            Type.DEVICE_STATE
                    )
        )
    }

    override fun fingerprint(flags: Int): String {
        val fingerprintSb = StringBuilder()
        val fingerprinters = LinkedList<BaseFingerprinter<*>>()

        if (flags and Type.HARDWARE != 0) {
            fingerprinters.add(hardwareFingerprinter)
        }

        if (flags and Type.OS_BUILD != 0) {
            fingerprinters.add(osBuildFingerprinter)
        }

        if (flags and Type.DEVICE_STATE != 0) {
            fingerprinters.add(deviceStateFingerprinter)
        }

        if (flags and Type.INSTALLED_APPS != 0) {
            fingerprinters.add(installedAppsFingerprinter)
        }

        fingerprinters.forEach {
            fingerprintSb.append(it.calculate())
        }

        return hasher.hash(fingerprintSb.toString())
    }

    override fun deviceIdProvider() = deviceIdProvider

    override fun hardwareFingerprinter() = hardwareFingerprinter

    override fun osBuildFingerprinter() = osBuildFingerprinter

    override fun installedAppsFingerprinter() = installedAppsFingerprinter

    override fun deviceStateFingerprinter() = deviceStateFingerprinter
}