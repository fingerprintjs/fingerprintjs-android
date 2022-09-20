package com.fingerprintjs.android.fingerprint.public_api.extended

import com.fingerprintjs.android.fingerprint.public_api.IdentificationVersion
import com.fingerprintjs.android.fingerprint.public_api.IdentificationVersionRange

public abstract class DeviceIdProvider internal constructor() {
    /**
     * Returns one of devices IDs listed below with respect to specified version
     */
    public abstract fun getDeviceIdMatching(
        @IdentificationVersionRange(from = IdentificationVersion.V_1) version: IdentificationVersion
    ): String

    // All device IDs:

    public abstract fun getGsfId(): String?
    public abstract fun getAndroidId(): String
    public abstract fun getMediaDrmId(): String
}
