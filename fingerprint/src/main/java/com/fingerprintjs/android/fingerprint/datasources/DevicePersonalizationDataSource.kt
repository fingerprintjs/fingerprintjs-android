package com.fingerprintjs.android.fingerprint.datasources

interface DevicePersonalisationDataSource {
    fun wallpaperSource(): String
    fun ringtoneSource(): String
    fun availableLocales(): String
}