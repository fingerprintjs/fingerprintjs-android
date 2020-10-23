package com.fingerprintjs.android.fingerprint

import com.fingerprintjs.android.fingerprint.device_id_providers.DeviceIdProvider
import com.fingerprintjs.android.fingerprint.fingerprinters.HardwareFingerprinter
import com.fingerprintjs.android.fingerprint.fingerprinters.OsBuildFingerprinter

class FingerprintAndroidAgentImpl(
    private val hardwareFingerprinter: HardwareFingerprinter,
    private val osBuildFingerprinter: OsBuildFingerprinter,
    private val deviceIdProvider: DeviceIdProvider
) : FingerprintAndroidAgent {

    override fun deviceId() = deviceIdProvider.getDeviceId()

    override fun getFingerprint(flags: Int) {
        TODO("Not yet implemented")
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