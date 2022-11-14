package com.fingerprintjs.android.playground.utils

import com.fingerprintjs.android.fingerprint.DeviceIdResult
import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher
import com.fingerprintjs.android.fingerprint.tools.hashers.MurMur3x64x128Hasher
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend fun Fingerprinter.getFingerprint(
    version: Fingerprinter.Version,
    stabilityLevel: StabilityLevel = StabilityLevel.OPTIMAL,
    hasher: Hasher = MurMur3x64x128Hasher(),
): String {
    return suspendCancellableCoroutine { cancellableContinuation ->
        this.getFingerprint(
            version = version,
            stabilityLevel = stabilityLevel,
            hasher = hasher,
            listener = { cancellableContinuation.resume(it) }
        )

    }
}

suspend fun Fingerprinter.getDeviceId(version: Fingerprinter.Version): DeviceIdResult {
    return suspendCancellableCoroutine { cancellableContinuation ->
        this.getDeviceId(
            version = version,
            listener = { cancellableContinuation.resume(it) }
        )
    }
}
