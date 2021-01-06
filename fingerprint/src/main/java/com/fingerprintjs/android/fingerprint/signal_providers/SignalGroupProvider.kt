package com.fingerprintjs.android.fingerprint.signal_providers


abstract class SignalGroupProvider<T>(
    val version: Int
) {
    abstract fun fingerprint(): String
    abstract fun rawData(): T
}

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