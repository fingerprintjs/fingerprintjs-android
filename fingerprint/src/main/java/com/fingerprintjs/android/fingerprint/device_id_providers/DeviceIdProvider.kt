package com.fingerprintjs.android.fingerprint.device_id_providers


interface DeviceIdProvider {
    fun getDeviceId(): String
}

class DeviceIdProviderImpl(
    private val gsfIdProvider: GsfIdProvider,
    private val androidIdProvider: AndroidIdProvider
) : DeviceIdProvider {
    override fun getDeviceId(): String {
        return gsfIdProvider.getGsfAndroidId() ?: androidIdProvider.getAndroidId()
    }
}