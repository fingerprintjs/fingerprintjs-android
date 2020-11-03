package com.fingerprintjs.android.fingerprint.datasources


import android.os.Build
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import com.fingerprintjs.android.fingerprint.tools.executeSafe


interface FingerprintSensorInfoProvider {
    fun getStatus(): FingerprintSensorStatus
}

class FingerprintSensorInfoProviderImpl(
    private val fingerprintManager: FingerprintManagerCompat
) : FingerprintSensorInfoProvider {
    override fun getStatus(): FingerprintSensorStatus {
        return executeSafe(
            {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    FingerprintSensorStatus.NOT_SUPPORTED
                } else if (!fingerprintManager.isHardwareDetected) {
                    FingerprintSensorStatus.NOT_SUPPORTED
                } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                    FingerprintSensorStatus.SUPPORTED
                } else {
                    FingerprintSensorStatus.ENABLED
                }
            }, FingerprintSensorStatus.UNKNOWN
        )
    }
}


enum class FingerprintSensorStatus(
    val stringDescription: String
) {
    NOT_SUPPORTED("not_supported"),
    SUPPORTED("supported"),
    ENABLED("enabled"),
    UNKNOWN("unknown")
}