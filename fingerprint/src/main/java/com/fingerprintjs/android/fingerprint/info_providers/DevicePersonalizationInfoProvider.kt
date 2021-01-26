package com.fingerprintjs.android.fingerprint.info_providers


import android.content.res.AssetManager
import android.content.res.Configuration
import android.media.RingtoneManager
import com.fingerprintjs.android.fingerprint.tools.executeSafe
import java.util.Locale
import java.util.TimeZone


interface DevicePersonalizationInfoProvider {
    fun ringtoneSource(): String
    fun availableLocales(): Array<String>
    fun regionCountry(): String
    fun defaultLanguage(): String
    fun timezone(): String
}

class DevicePersonalizationInfoProviderImpl(
    private val ringtoneManager: RingtoneManager,
    private val assetManager: AssetManager,
    private val configuration: Configuration
) : DevicePersonalizationInfoProvider {

    override fun ringtoneSource(): String {
        return executeSafe({ ringtoneManager.getRingtoneUri(0).toString() }, "")
    }

    override fun availableLocales(): Array<String> {
        return executeSafe({ assetManager.locales }, emptyArray())
    }

    @Suppress("DEPRECATION")
    override fun regionCountry(): String {
        return executeSafe({ configuration.locale.country }, "")
    }

    override fun defaultLanguage(): String {
        return Locale.getDefault().language
    }

    override fun timezone(): String {
        return executeSafe({ TimeZone.getDefault().displayName }, "")
    }

}