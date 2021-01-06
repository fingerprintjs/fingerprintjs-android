package com.fingerprintjs.android.fingerprint.info_providers


import android.app.ActivityManager
import com.fingerprintjs.android.fingerprint.tools.executeSafe


interface GpuInfoProvider {
    fun glesVersion(): String
    fun vendor(): String
    fun renderer(): String
    fun extensions(): List<String>
}

class GpuInfoProviderImpl(
    private val activityManager: ActivityManager
) : GpuInfoProvider {

    private var vendor: String? = null
    private val renderer: String? = null
    private val extensions: List<String>? = null

    init {

    }
    override fun glesVersion(): String {
        return executeSafe({ activityManager.deviceConfigurationInfo.glEsVersion }, "")
    }

    override fun vendor(): String {
        TODO("Not yet implemented")
    }

    override fun renderer(): String {
        TODO("Not yet implemented")
    }

    override fun extensions(): List<String> {
        TODO("Not yet implemented")
    }

}