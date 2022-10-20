package com.fingerprintjs.android.fingerprint.fingerprinting_signals

import com.fingerprintjs.android.fingerprint.IdentificationVersion
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel

/**
 * Represents a signal used for device fingerprinting.
 */
public sealed class FingerprintingSignal<out T> {
    /**
     * "Meta" information about the signal. Each signal must
     * also contain a static version of this property.
     */
    public abstract val info: Info
    /**
     * An actual data that this signal holds
     */
    public abstract val value: T
    /**
     * @return A string that is usable for device fingerprinting.
     */
    public abstract fun getHashableString(): String

    public data class Info(
        public val addedInVersion: IdentificationVersion,
        public val removedInVersion: IdentificationVersion?,
        public val stabilityLevel: StabilityLevel,
    )
}
