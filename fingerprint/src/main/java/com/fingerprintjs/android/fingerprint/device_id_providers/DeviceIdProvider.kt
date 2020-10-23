package com.fingerprintjs.android.fingerprint.device_id_providers


import android.provider.Settings


interface DeviceIdProvider {
    fun getDeviceId(): String
}

class DeviceIdProviderImpl(
    private val gsfIdProvider: GsfIdProvider
) : DeviceIdProvider {
    override fun getDeviceId(): String {
        return gsfIdProvider.getGsfAndroidId() ?: Settings.Secure.ANDROID_ID
    }
}