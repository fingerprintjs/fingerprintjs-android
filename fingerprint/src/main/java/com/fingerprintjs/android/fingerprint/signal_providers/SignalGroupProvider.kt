package com.fingerprintjs.android.fingerprint.signal_providers


object SignalGroupProviderType {
    @JvmField
    val HARDWARE = 1

    @JvmField
    val OS_BUILD = 1 shl 1

    @JvmField
    val INSTALLED_APPS = 1 shl 2

    @JvmField
    val DEVICE_STATE = 1 shl 3
}

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
        signals: List<Signal<*>>,
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