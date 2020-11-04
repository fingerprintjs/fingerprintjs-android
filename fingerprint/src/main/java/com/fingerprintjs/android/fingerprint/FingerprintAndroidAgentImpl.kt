package com.fingerprintjs.android.fingerprint


import com.fingerprintjs.android.fingerprint.device_id_providers.DeviceIdProvider
import com.fingerprintjs.android.fingerprint.fingerprinters.Fingerprinter
import com.fingerprintjs.android.fingerprint.fingerprinters.device_state.DeviceStateFingerprinter
import com.fingerprintjs.android.fingerprint.fingerprinters.hardware.HardwareFingerprinter
import com.fingerprintjs.android.fingerprint.fingerprinters.installed_apps.InstalledAppsFingerprinter
import com.fingerprintjs.android.fingerprint.fingerprinters.os_build_fingerprint.OsBuildFingerprinter
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher
import java.util.LinkedList


class FingerprintAndroidAgentImpl(
    private val hardwareFingerprinter: HardwareFingerprinter,
    private val osBuildFingerprinter: OsBuildFingerprinter,
    private val deviceIdProvider: DeviceIdProvider,
    private val installedAppsFingerprinter: InstalledAppsFingerprinter,
    private val deviceStateFingerprinter: DeviceStateFingerprinter,
    private val hasher: Hasher
) : FingerprintAndroidAgent {

    override fun deviceId() = deviceIdProvider.getDeviceId()

    override fun getFingerprint(flags: Int): String {
        val fingerprintSb = StringBuilder()
        val fingerprinters = LinkedList<Fingerprinter<*>>()

        if (flags and FingerprintAndroidAgent.HARDWARE != 0) {
            fingerprinters.add(hardwareFingerprinter)
        }

        if (flags and FingerprintAndroidAgent.OS_BUILD != 0) {
            fingerprinters.add(osBuildFingerprinter)
        }

        if (flags and FingerprintAndroidAgent.DEVICE_STATE != 0) {
            fingerprinters.add(deviceStateFingerprinter)
        }

        if (flags and FingerprintAndroidAgent.INSTALLED_APPS != 0) {
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