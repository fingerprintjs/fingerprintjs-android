package com.fingerprintjs.android.fingerprint


import com.fingerprintjs.android.fingerprint.device_id_providers.DeviceIdProvider
import com.fingerprintjs.android.fingerprint.fingerprinters.DeviceStateFingerprinter
import com.fingerprintjs.android.fingerprint.fingerprinters.Fingerprinter
import com.fingerprintjs.android.fingerprint.fingerprinters.HardwareFingerprinter
import com.fingerprintjs.android.fingerprint.fingerprinters.InstalledAppsFingerprinter
import com.fingerprintjs.android.fingerprint.fingerprinters.OsBuildFingerprinter
import com.fingerprintjs.android.fingerprint.hashers.Hasher
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
        val fingerprinters = LinkedList<Fingerprinter>()

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

    override fun hardwareFingerprint() = hardwareFingerprinter.calculate()

    override fun osBuildFingerprint() = osBuildFingerprinter.calculate()

    override fun installedAppsFingerprint() = installedAppsFingerprinter.calculate()

    override fun deviceStateFingerprint() = deviceStateFingerprinter.calculate()
}