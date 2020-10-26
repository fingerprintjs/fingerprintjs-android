package com.fingerprintjs.android.fingerprint


import com.fingerprintjs.android.fingerprint.device_id_providers.DeviceIdProvider
import com.fingerprintjs.android.fingerprint.fingerprinters.HardwareFingerprinter
import com.fingerprintjs.android.fingerprint.fingerprinters.OsBuildFingerprinter
import com.fingerprintjs.android.fingerprint.hashers.Hasher


class FingerprintAndroidAgentImpl(
    private val hardwareFingerprinter: HardwareFingerprinter,
    private val osBuildFingerprinter: OsBuildFingerprinter,
    private val deviceIdProvider: DeviceIdProvider,
    private val hasher: Hasher
) : FingerprintAndroidAgent {

    override fun deviceId() = deviceIdProvider.getDeviceId()

    override fun getFingerprint(flags: Int): String {
        val fingerprintSb = StringBuilder()
        if (flags and FingerprintAndroidAgent.HARDWARE != 0) {
            fingerprintSb.append(hardwareFingerprint())
        }

        if (flags and FingerprintAndroidAgent.OS_BUILD != 0) {
            fingerprintSb.append(osBuildFingerprint())
        }

        if (flags and FingerprintAndroidAgent.DEVICE_STATE != 0) {
            fingerprintSb.append(deviceStateFingerprint())
        }

        if (flags and FingerprintAndroidAgent.INSTALLED_APPS != 0) {
            fingerprintSb.append(installedAppsFingerprint())
        }

        return hasher.hash(fingerprintSb.toString())
    }

    override fun hardwareFingerprint() = hardwareFingerprinter.calculate()

    override fun osBuildFingerprint() = osBuildFingerprinter.calculate()


    override fun installedAppsFingerprint(): String {
        TODO("Not yet implemented")
    }

    override fun deviceStateFingerprint(): String {
        TODO("Not yet implemented")
    }
}