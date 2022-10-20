package com.fingerprintjs.android.fingerprint.info_providers


import android.os.Build
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages
import com.fingerprintjs.android.fingerprint.tools.executeSafe


@Deprecated(message = DeprecationMessages.UNREACHABLE_SYMBOL_UNINTENDED_PUBLIC_API)
public interface FingerprintSensorInfoProvider {
    public fun getStatus(): FingerprintSensorStatus
}

internal class FingerprintSensorInfoProviderImpl(
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
            },
            FingerprintSensorStatus.UNKNOWN
        )
    }
}


@Deprecated(message = DeprecationMessages.UNREACHABLE_SYMBOL_UNINTENDED_PUBLIC_API)
public enum class FingerprintSensorStatus(
    public val stringDescription: String
) {
    NOT_SUPPORTED("not_supported"),
    SUPPORTED("supported"),
    ENABLED("enabled"),
    UNKNOWN("unknown")
}