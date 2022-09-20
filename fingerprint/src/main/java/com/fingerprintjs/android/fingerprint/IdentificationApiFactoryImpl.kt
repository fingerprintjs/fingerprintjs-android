package com.fingerprintjs.android.fingerprint

import android.content.Context
import com.fingerprintjs.android.fingerprint.public_api.IdentificationApi
import com.fingerprintjs.android.fingerprint.public_api.IdentificationApiFactory

internal class IdentificationApiFactoryImpl: IdentificationApiFactory() {
    override fun create(context: Context, configuration: Configuration): IdentificationApi {
        TODO("Not yet implemented")
    }
}
