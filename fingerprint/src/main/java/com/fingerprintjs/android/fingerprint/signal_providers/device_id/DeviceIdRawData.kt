package com.fingerprintjs.android.fingerprint.signal_providers.device_id


import com.fingerprintjs.android.fingerprint.signal_providers.IdentificationSignal
import com.fingerprintjs.android.fingerprint.signal_providers.RawData
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages


@Deprecated(message = DeprecationMessages.DEPRECATED_SYMBOL)
public class DeviceIdRawData(
    private val androidId: String,
    private val gsfId: String?,
    private val mediaDrmId: String?
) : RawData() {

    override fun signals(): List<IdentificationSignal<String>> = listOf(
        gsfId(),
        androidId(),
        mediaDrmId()
    )

    public fun gsfId(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        1,
        null,
        StabilityLevel.STABLE,
        GSF_ID_NAME,
        GSF_ID_DISPLAY_NAME,
        gsfId ?: ""
    ) {
        override fun toString() = gsfId ?: ""
    }

    public fun androidId(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        1,
        null,
        StabilityLevel.STABLE,
        ANDROID_ID_NAME,
        ANDROID_ID_DISPLAY_NAME,
        androidId
    ) {
        override fun toString() = androidId
    }

    public fun mediaDrmId(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        3,
        null,
        StabilityLevel.STABLE,
        MEDIA_DRM_NAME,
        MEDIA_DRM_DISPLAY_NAME,
        mediaDrmId ?: ""
    ) {
        override fun toString() = mediaDrmId ?: ""
    }
}