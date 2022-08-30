package com.fingerprintjs.android.fingerprint.signal_providers


enum class StabilityLevel {
    STABLE,
    OPTIMAL,
    UNIQUE
}

/**
 * What is a purpose of this class?
 * Group of signals is provided by RawData (which is not obvious from it's name btw)
 *
 * combineSignals is a method used solely for util purposes, as well as fingerprint method
 */
abstract class SignalGroupProvider<out T : RawData>(
    val version: Int
) {
    abstract fun fingerprint(stabilityLevel: StabilityLevel = StabilityLevel.OPTIMAL): String
    abstract fun rawData(): T

    // why version is not taken as parameter here?
    protected fun combineSignals(
        signals: List<IdentificationSignal<*>>,
        stabilityLevel: StabilityLevel
    ): String {
        val sb = StringBuilder()
        signals
            .filterByStabilityLevel(stabilityLevel)
            .forEach {
                sb.append(it.toString())
            }
        return sb.toString()
    }
}