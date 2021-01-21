package com.fingerprintjs.android.fingerprint.signal_providers


abstract class RawData {
    abstract fun signals(): List<Signal<*>>
    fun signals(version: Int, stabilityLevel: StabilityLevel) = signals()
        .filterByStabilityLevel(
            stabilityLevel
        )
        .filterByVersion(version)
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

fun List<Signal<*>>.filterByVersion(version: Int): List<Signal<*>> {
    return this.filter {
        val isNotRemoved =
            ((it.removedInVersion == null) || ((it.removedInVersion >= version)))
        val enableInVersion = it.addedInVersion in 1..version
        isNotRemoved && enableInVersion
    }
}
