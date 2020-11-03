package com.fingerprintjs.android.fingerprint.datasources


import android.content.res.AssetManager
import android.media.RingtoneManager
import com.fingerprintjs.android.fingerprint.tools.executeSafe


interface DevicePersonalizationDataSource {
    fun ringtoneSource(): String
    fun availableLocales(): Array<String>
}

class DevicePersonalizationDataSourceImpl(
    private val ringtoneManager: RingtoneManager,
    private val assetManager: AssetManager
) : DevicePersonalizationDataSource {

    override fun ringtoneSource(): String {
        return executeSafe({ ringtoneManager.getRingtoneUri(0).toString() }, "")
    }

    override fun availableLocales(): Array<String> {
        return executeSafe({ assetManager.locales }, emptyArray())
    }

}