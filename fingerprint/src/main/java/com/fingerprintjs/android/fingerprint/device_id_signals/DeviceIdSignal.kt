package com.fingerprintjs.android.fingerprint.device_id_signals

internal sealed class DeviceIdSignal<T>() {
    abstract val value: T
    abstract fun getIdString(): String
}
