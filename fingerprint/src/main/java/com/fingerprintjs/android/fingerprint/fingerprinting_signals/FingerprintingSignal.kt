package com.fingerprintjs.android.fingerprint.fingerprinting_signals

import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel

/**
 * Represents a signal used for device fingerprinting.
 */
public sealed class FingerprintingSignal<out T> {
    /**
     * "Meta" information about the signal. Must reference
     * a static version of this property which must also be exposed.
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
        public val addedInVersion: Fingerprinter.Version,
        public val removedInVersion: Fingerprinter.Version?,
        public val stabilityLevel: StabilityLevel,
    )
}
