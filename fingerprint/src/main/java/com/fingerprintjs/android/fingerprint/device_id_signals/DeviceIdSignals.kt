package com.fingerprintjs.android.fingerprint.device_id_signals

internal class GsfIdSignal(
    override val value: String
): DeviceIdSignal<String>() {
    override fun getIdString(): String = value
}

internal class AndroidIdSignal(
    override val value: String
): DeviceIdSignal<String>() {
    override fun getIdString(): String = value
}

internal class MediaDrmIdSignal(
    override val value: String
): DeviceIdSignal<String>() {
    override fun getIdString(): String = value
}
