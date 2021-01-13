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