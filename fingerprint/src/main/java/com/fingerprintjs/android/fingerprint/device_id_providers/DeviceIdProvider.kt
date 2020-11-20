package com.fingerprintjs.android.fingerprint.device_id_providers


interface DeviceIdProvider {
    fun getDeviceId(): String
    fun getAndroidId(): String
    fun getGsfId(): String?
}

class DeviceIdProviderImpl(
    private val gsfIdProvider: GsfIdProvider,
    private val androidIdProvider: AndroidIdProvider
) : DeviceIdProvider {
    override fun getDeviceId(): String {
        return getGsfId() ?: getAndroidId()
    }

    override fun getAndroidId() = androidIdProvider.getAndroidId()

    override fun getGsfId() = gsfIdProvider.getGsfAndroidId()
}