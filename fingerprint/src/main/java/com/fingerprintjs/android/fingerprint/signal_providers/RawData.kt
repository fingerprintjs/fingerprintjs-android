package com.fingerprintjs.android.fingerprint.signal_providers


abstract class RawData {
    abstract fun signals(): List<IdentificationSignal<*>>
    fun signals(version: Int, stabilityLevel: StabilityLevel) = signals()
        .filterByStabilityLevel(
            stabilityLevel
        )
        .filterByVersion(version)
}

fun List<IdentificationSignal<*>>.filterByStabilityLevel(stabilityLevel: StabilityLevel): List<IdentificationSignal<*>> {
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

fun List<IdentificationSignal<*>>.filterByVersion(version: Int): List<IdentificationSignal<*>> {
    return this.filter {
        val isNotRemoved =
            ((it.removedInVersion == null) || ((it.removedInVersion > version)))
        val enabledInVersion = it.addedInVersion in 1..version
        isNotRemoved && enabledInVersion
    }
}
