package com.fingerprintjs.android.playground.utils.mappers

import com.fingerprintjs.android.fingerprint.DeviceIdResult
import com.fingerprintjs.android.playground.constants.Constants

const val ANDROID_ID_HUMAN_NAME = "Android ID"
const val GSF_ID_HUMAN_NAME = "GSF ID"
const val MEDIA_DRM_ID_HUMAN_NAME = "Media DRM ID"

val DeviceIdResult.deviceIdPrettified: String
    get() = this.deviceId.orUnknown()
val DeviceIdResult.gsfIdPrettified: String
    get() = this.gsfId.orUnknown()
val DeviceIdResult.androidIdPrettified: String
    get() = this.androidId.orUnknown()
val DeviceIdResult.mediaDrmIdPrettified: String
    get() = this.mediaDrmId.orUnknown()

private fun String.orUnknown(): String =
    this.takeIf { it.isNotEmpty() } ?: Constants.SIGNAL_UNKNOWN_VALUE
