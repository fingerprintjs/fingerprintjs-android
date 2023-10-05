package com.fingerprintjs.android.fingerprint.device_id_providers


import android.annotation.SuppressLint
import android.content.ContentResolver
import android.provider.Settings
import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages
import com.fingerprintjs.android.fingerprint.tools.threading.safe.safe


@Deprecated(message = DeprecationMessages.UNREACHABLE_SYMBOL_UNINTENDED_PUBLIC_API)
public class AndroidIdProvider(
    private val contentResolver: ContentResolver?
) {
    @SuppressLint("HardwareIds")
    public fun getAndroidId(): String {
        return safe {
            Settings.Secure.getString(
                contentResolver!!,
                Settings.Secure.ANDROID_ID
            )!!
        }.getOrDefault("")
    }
}