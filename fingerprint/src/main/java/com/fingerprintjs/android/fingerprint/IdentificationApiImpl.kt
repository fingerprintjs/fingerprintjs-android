package com.fingerprintjs.android.fingerprint

import com.fingerprintjs.android.fingerprint.public_api.IdentificationApi
import com.fingerprintjs.android.fingerprint.public_api.extended.IdentificationExtendedApi

internal class IdentificationApiImpl: IdentificationApi() {
    override fun getExtendedApi(): IdentificationExtendedApi {
        TODO("Not yet implemented")
    }
}
