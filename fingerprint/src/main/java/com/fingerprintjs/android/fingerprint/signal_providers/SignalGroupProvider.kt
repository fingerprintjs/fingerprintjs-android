package com.fingerprintjs.android.fingerprint.signal_providers


enum class StabilityLevel {
    STABLE,
    OPTIMAL,
    UNIQUE
}

abstract class SignalGroupProvider<T : RawData>(
    val version: Int
) {
    abstract fun fingerprint(stabilityLevel: StabilityLevel = StabilityLevel.OPTIMAL): String
    abstract fun rawData(): T

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