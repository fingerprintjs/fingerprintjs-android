package com.fingerprintjs.android.fingerprint.info_providers


import android.content.res.AssetManager
import android.content.res.Configuration
import android.media.RingtoneManager
import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages
import com.fingerprintjs.android.fingerprint.tools.safe.SafeLazy
import com.fingerprintjs.android.fingerprint.tools.safe.safe
import java.util.Locale
import java.util.TimeZone


@Deprecated(message = DeprecationMessages.UNREACHABLE_SYMBOL_UNINTENDED_PUBLIC_API)
public interface DevicePersonalizationInfoProvider {
    public fun ringtoneSource(): String
    public fun availableLocales(): Array<String> // // theoretically, may contain "null" strings for backwards compatibility reasons
    public fun regionCountry(): String
    public fun defaultLanguage(): String
    public fun timezone(): String
}

internal class DevicePersonalizationInfoProviderImpl(
    private val ringtoneManager: SafeLazy<RingtoneManager>,
    private val assetManager: SafeLazy<AssetManager>,
    private val configuration: SafeLazy<Configuration>,
) : DevicePersonalizationInfoProvider {

    override fun ringtoneSource(): String {
        return safe{ ringtoneManager.getOrThrow().getRingtoneUri(0)!!.toString()!! }.getOrDefault("")
    }

    override fun availableLocales(): Array<String> {
        return safe {
            assetManager.getOrThrow().locales!!
                .map { locale: String? -> locale.toString() }.toTypedArray()
        }.getOrDefault(emptyArray())
    }

    @Suppress("DEPRECATION")
    override fun regionCountry(): String {
        return safe{ configuration.getOrThrow().locale!!.country!! }.getOrDefault("")
    }

    override fun defaultLanguage(): String {
        return safe { Locale.getDefault()!!.language!! } .getOrDefault("")
    }

    override fun timezone(): String {
        return safe { TimeZone.getDefault()!!.displayName!! }.getOrDefault("")
    }
}