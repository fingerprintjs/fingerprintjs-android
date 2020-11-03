package com.fingerprintjs.android.fingerprint.device_id_providers


import android.annotation.SuppressLint
import android.content.ContentResolver
import android.provider.Settings
import com.fingerprintjs.android.fingerprint.tools.executeSafe


class AndroidIdProvider(
    private val contentResolver: ContentResolver
) {
    @SuppressLint("HardwareIds")
    fun getAndroidId(): String {
        return executeSafe({
            Settings.Secure.getString(
                contentResolver,
                Settings.Secure.ANDROID_ID
            )
        }, "")
    }
}