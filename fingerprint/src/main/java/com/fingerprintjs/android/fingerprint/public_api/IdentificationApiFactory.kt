package com.fingerprintjs.android.fingerprint.public_api

import android.content.Context

public abstract class IdentificationApiFactory internal constructor() {
    public abstract fun create(
        context: Context,
    ): IdentificationApi
}
