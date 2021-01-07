package com.fingerprintjs.android.fingerprint.info_providers


import android.app.ActivityManager
import com.fingerprintjs.android.fingerprint.tools.executeSafe


interface GpuInfoProvider {
    fun glesVersion(): String
}

class GpuInfoProviderImpl(
    private val activityManager: ActivityManager
) : GpuInfoProvider {

    private var vendor: String? = null
    private val renderer: String? = null
    private val extensions: List<String>? = null

    override fun glesVersion(): String {
        return executeSafe({ activityManager.deviceConfigurationInfo.glEsVersion }, "")
    }
}