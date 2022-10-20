package com.fingerprintjs.android.fingerprint.signal_providers

import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages


@Deprecated(message = DeprecationMessages.DEPRECATED_SYMBOL)
public abstract class SignalGroupProvider<out T : RawData>(
    public val version: Int
) {
    public abstract fun fingerprint(stabilityLevel: StabilityLevel = StabilityLevel.OPTIMAL): String
    public abstract fun rawData(): T

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