package com.fingerprintjs.android.fingerprint.device_id_providers


import android.annotation.SuppressLint
import android.content.ContentResolver
import android.provider.Settings


class AndroidIdProvider(
    private val contentResolver: ContentResolver
) {
    @SuppressLint("HardwareIds")
    fun getAndroidId(): String {
        return Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }
}