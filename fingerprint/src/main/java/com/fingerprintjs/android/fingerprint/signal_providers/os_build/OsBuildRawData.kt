package com.fingerprintjs.android.fingerprint.signal_providers.os_build


import com.fingerprintjs.android.fingerprint.info_providers.MediaCodecInfo
import com.fingerprintjs.android.fingerprint.signal_providers.IdentificationSignal
import com.fingerprintjs.android.fingerprint.signal_providers.RawData
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages


@Deprecated(message = DeprecationMessages.DEPRECATED_SYMBOL)
public data class OsBuildRawData(
    val fingerprint: String,
    val androidVersion: String,
    val sdkVersion: String,
    val kernelVersion: String,
    val codecList: List<MediaCodecInfo>,
    val encryptionStatus: String,
    val securityProvidersData: List<Pair<String, String>>
) : RawData() {

    override fun signals(): List<IdentificationSignal<out Any>> = listOf(
        fingerprint(),
        androidVersion(),
        sdkVersion(),
        kernelVersion(),
        codecList(),
        encryptionStatus(),
        securityProviders()
    )

    public fun fingerprint(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        1,
        2,
        StabilityLevel.OPTIMAL,
        FINGERPRINT_KEY,
        FINGERPRINT_DISPLAY_NAME,
        fingerprint
    ) {
        override fun toString() = fingerprint
    }

    public fun androidVersion(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        2,
        null,
        StabilityLevel.OPTIMAL,
        ANDROID_VERSION_KEY,
        ANDROID_VERSION_DISPLAY_NAME,
        androidVersion
    ) {
        override fun toString() = androidVersion
    }

    public fun sdkVersion(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        2,
        null,
        StabilityLevel.OPTIMAL,
        SDK_VERSION_KEY,
        SDK_VERSION_DISPLAY_NAME,
        sdkVersion
    ) {
        override fun toString() = sdkVersion
    }

    public fun kernelVersion(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        2,
        null,
        StabilityLevel.OPTIMAL,
        KERNEL_VERSION_KEY,
        KERNEL_VERSION_DISPLAY_NAME,
        kernelVersion
    ) {
        override fun toString() = kernelVersion
    }

    public fun encryptionStatus(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        2,
        null,
        StabilityLevel.OPTIMAL,
        ENCRYPTION_STATUS_KEY,
        ENCRYPTION_STATUS_DISPLAY_NAME,
        encryptionStatus
    ) {
        override fun toString() = encryptionStatus
    }

    public fun codecList(): IdentificationSignal<List<MediaCodecInfo>> = object : IdentificationSignal<List<MediaCodecInfo>>(
        2,
        null,
        StabilityLevel.OPTIMAL,
        CODEC_LIST_KEY,
        CODEC_LIST_DISPLAY_NAME,
        codecList
    ) {
        override fun toString(): String {
            val sb = StringBuilder()

            codecList.forEach {
                sb.append(it.name)
                it.capabilities.forEach { capability ->
                    sb.append(capability)
                }
            }

            return sb.toString()
        }
    }

    public fun securityProviders(): IdentificationSignal<List<Pair<String, String>>> = object : IdentificationSignal<List<Pair<String, String>>>(
        2,
        null,
        StabilityLevel.OPTIMAL,
        SECURITY_PROVIDERS_DATA_KEY,
        SECURITY_PROVIDERS_DATA_DISPLAY_NAME,
        securityProvidersData
    ) {
        override fun toString(): String {
            val sb = StringBuilder()

            securityProvidersData.forEach {
                sb
                    .append(it.first)
                    .append(it.second)
            }

            return sb.toString()
        }
    }
}