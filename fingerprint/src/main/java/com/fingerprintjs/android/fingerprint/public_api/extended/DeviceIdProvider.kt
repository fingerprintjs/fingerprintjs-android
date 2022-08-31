package com.fingerprintjs.android.fingerprint.public_api.extended

public interface DeviceIdProvider {
    public fun getGsfId(): String?
    public fun getAndroidId(): String
    public fun getMediaDrmId(): String
}