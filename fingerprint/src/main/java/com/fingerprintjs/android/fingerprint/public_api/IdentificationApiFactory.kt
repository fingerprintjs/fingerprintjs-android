package com.fingerprintjs.android.fingerprint.public_api

import android.content.Context
import com.fingerprintjs.android.fingerprint.Configuration

public abstract class IdentificationApiFactory internal constructor() {
    public abstract fun create(
        context: Context,
        configuration: Configuration,
    ): IdentificationApi
}
