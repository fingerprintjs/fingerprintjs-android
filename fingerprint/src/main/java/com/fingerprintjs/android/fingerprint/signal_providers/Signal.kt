package com.fingerprintjs.android.fingerprint.signal_providers


abstract class Signal<T>(
    val addedInVersion: Int,
    val removedInVersion: Int?,
    val stabilityLevel: StabilityLevel,
    val name: String,
    val displayName: String,
    val value: T
) {
    abstract override fun toString(): String
}


fun List<Signal<*>>.filterByStabilityLevel(stabilityLevel: StabilityLevel): List<Signal<*>> {
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