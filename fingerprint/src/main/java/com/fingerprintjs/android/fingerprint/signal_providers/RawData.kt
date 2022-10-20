package com.fingerprintjs.android.fingerprint.signal_providers

import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages


@Deprecated(message = DeprecationMessages.DEPRECATED_SYMBOL)
public abstract class RawData {
    public abstract fun signals(): List<IdentificationSignal<*>>
    public fun signals(version: Int, stabilityLevel: StabilityLevel): List<IdentificationSignal<*>> = signals()
        .filterByStabilityLevel(
            stabilityLevel
        )
        .filterByVersion(version)
}

@Deprecated(message = DeprecationMessages.DEPRECATED_SYMBOL)
public fun List<IdentificationSignal<*>>.filterByStabilityLevel(stabilityLevel: StabilityLevel): List<IdentificationSignal<*>> {
    return this.filter {
        when (stabilityLevel) {
            StabilityLevel.STABLE -> {
                it.stabilityLevel == StabilityLevel.STABLE
            }
            StabilityLevel.OPTIMAL -> {
                (it.stabilityLevel == StabilityLevel.STABLE) or (it.stabilityLevel == StabilityLevel.OPTIMAL)
            }
            StabilityLevel.UNIQUE -> {
                true
            }
        }
    }
}

@Deprecated(message = DeprecationMessages.DEPRECATED_SYMBOL)
public fun List<IdentificationSignal<*>>.filterByVersion(version: Int): List<IdentificationSignal<*>> {
    return this.filter {
        val isNotRemoved =
            ((it.removedInVersion == null) || ((it.removedInVersion > version)))
        val enabledInVersion = it.addedInVersion in 1..version
        isNotRemoved && enabledInVersion
    }
}
