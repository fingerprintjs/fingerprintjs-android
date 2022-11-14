package com.fingerprintjs.android.playground.usecases.report

import com.google.gson.annotations.SerializedName

data class ReportAttachmentVo(
    @SerializedName("appVersionName") val appVersionName: String,
    @SerializedName("appVersionCode") val appVersionCode: Int,
    @SerializedName("fingerprints") val fingerprints: List<FingerprintInfoVo>,
    @SerializedName("deviceIds") val deviceIds: List<DeviceIdInfoVo>,
)

data class FingerprintInfoVo(
    @SerializedName("version") val version: String,
    @SerializedName("stabilityLevel") val stabilityLevel: String,
    @SerializedName("fingerprint") val fingerprint: String,
    @SerializedName("signals") val signals: List<FingerprintSignalVo>,
)

data class FingerprintSignalVo(
    @SerializedName("name") val name: String,
    @SerializedName("value") val value: Any,
)

data class DeviceIdInfoVo(
    @SerializedName("version") val version: String,
    @SerializedName("deviceID") val deviceID: String,
    @SerializedName("signals") val signals: List<DeviceIdSignalVo>,
)

data class DeviceIdSignalVo(
    @SerializedName("name") val name: String,
    @SerializedName("value") val value: String,
)
