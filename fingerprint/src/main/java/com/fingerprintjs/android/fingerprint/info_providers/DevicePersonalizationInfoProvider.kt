package com.fingerprintjs.android.fingerprint.info_providers


import android.content.res.AssetManager
import android.content.res.Configuration
import android.media.RingtoneManager
import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages
import com.fingerprintjs.android.fingerprint.tools.executeSafe
import java.util.Locale
import java.util.TimeZone


@Deprecated(message = DeprecationMessages.UNREACHABLE_SYMBOL_UNINTENDED_PUBLIC_API)
public interface DevicePersonalizationInfoProvider {
    public fun ringtoneSource(): String
    public fun availableLocales(): Array<String>
    public fun regionCountry(): String
    public fun defaultLanguage(): String
    public fun timezone(): String
}

internal class DevicePersonalizationInfoProviderImpl(
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