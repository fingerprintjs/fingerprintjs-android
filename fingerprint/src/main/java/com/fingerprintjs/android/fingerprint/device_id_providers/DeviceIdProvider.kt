package com.fingerprintjs.android.fingerprint.device_id_providers


interface DeviceIdProvider {
    fun getDeviceId(): String
    fun getAndroidId(): String
    fun getGsfId(): String?
    fun getMediaDrmId(): String?
}

internal class DeviceIdProviderImpl(
    private val gsfIdProvider: GsfIdProvider,
    private val androidIdProvider: AndroidIdProvider,
    private val mediaDrmIdProvider: MediaDrmIdProvider
) : DeviceIdProvider {
    override fun getDeviceId(): String {
        return getGsfId() ?: getMediaDrmId() ?: getAndroidId()
    }

    override fun getAndroidId() = androidIdProvider.getAndroidId()

    override fun getGsfId() = gsfIdProvider.getGsfAndroidId()

    override fun getMediaDrmId() = mediaDrmIdProvider.getMediaDrmId()
}