package com.fingerprintjs.android.fingerprint.signal_providers

/**
 * A property which is assigned to each
 * [FingerprintingSignal][com.fingerprintjs.android.fingerprint.fingerprinting_signals.FingerprintingSignal]
 * or, previously, [IdentificationSignal][com.fingerprintjs.android.fingerprint.signal_providers.IdentificationSignal].
 * The more stable the signal is, the less likely it is to change over time for a particular device. Check out our github
 * page for more information.
 */
public enum class StabilityLevel {
    STABLE,
    OPTIMAL,
    UNIQUE;

    /**
     * We could use comparison operators instead, but the ordering of enums
     * is reversed and thus counter intuitive. We cannot change it for
     * backwards compatibility reasons.
     */
    internal fun atLeastAsStableAs(other: StabilityLevel): Boolean {
        return when (this) {
            STABLE -> true
            OPTIMAL -> when (other) {
                STABLE -> false
                OPTIMAL,
                UNIQUE -> true
            }
            UNIQUE -> when (other) {
                STABLE,
                OPTIMAL -> false
                UNIQUE -> true
            }
        }
    }
}
