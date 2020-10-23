package com.fingerprintjs.android.fingerprint.datasources


import android.os.Build


interface OsBuildInfoProvider {
    fun modelName(): String
    fun manufacturerName(): String
    fun fingerprint(): String
}

class OsBuildInfoProviderImpl : OsBuildInfoProvider {
    override fun modelName(): String {
        return Build.MODEL
    }

    override fun manufacturerName(): String {
        return Build.MANUFACTURER
    }

    override fun fingerprint(): String {
        return Build.FINGERPRINT
    }
}