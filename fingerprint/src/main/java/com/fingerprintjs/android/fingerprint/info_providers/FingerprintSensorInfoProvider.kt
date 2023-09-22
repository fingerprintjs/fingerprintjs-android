package com.fingerprintjs.android.fingerprint.info_providers


import android.os.Build
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages
import com.fingerprintjs.android.fingerprint.tools.safe.SafeLazy
import com.fingerprintjs.android.fingerprint.tools.safe.safe


@Deprecated(message = DeprecationMessages.UNREACHABLE_SYMBOL_UNINTENDED_PUBLIC_API)
public interface FingerprintSensorInfoProvider {
    public fun getStatus(): FingerprintSensorStatus
}

internal class FingerprintSensorInfoProviderImpl(
    private val fingerprintManager: SafeLazy<FingerprintManagerCompat>,
) : FingerprintSensorInfoProvider {
    override fun getStatus(): FingerprintSensorStatus {
        return safe {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                FingerprintSensorStatus.NOT_SUPPORTED
            } else if (!fingerprintManager.getOrThrow().isHardwareDetected) {
                FingerprintSensorStatus.NOT_SUPPORTED
            } else if (!fingerprintManager.getOrThrow().hasEnrolledFingerprints()) {
                FingerprintSensorStatus.SUPPORTED
            } else {
                FingerprintSensorStatus.ENABLED
            }
        }.getOrDefault(FingerprintSensorStatus.UNKNOWN)
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