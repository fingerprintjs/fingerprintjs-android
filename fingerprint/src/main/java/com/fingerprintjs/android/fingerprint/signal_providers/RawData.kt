package com.fingerprintjs.android.fingerprint.signal_providers


interface RawData {
    fun signals(): List<Signal<*>>
}
