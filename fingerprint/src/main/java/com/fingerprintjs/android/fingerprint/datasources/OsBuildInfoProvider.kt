package com.fingerprintjs.android.fingerprint.datasources


import android.os.Build
import com.fingerprintjs.android.fingerprint.tools.executeSafe


interface OsBuildInfoProvider {
    fun modelName(): String
    fun manufacturerName(): String
    fun fingerprint(): String
}

class OsBuildInfoProviderImpl : OsBuildInfoProvider {
    override fun modelName(): String {
        return executeSafe( { Build.MODEL }, "")
    }

    override fun manufacturerName(): String {
        return executeSafe( { Build.MANUFACTURER }, "")
    }

    override fun fingerprint(): String {
        return executeSafe( { Build.FINGERPRINT }, "")
    }
}