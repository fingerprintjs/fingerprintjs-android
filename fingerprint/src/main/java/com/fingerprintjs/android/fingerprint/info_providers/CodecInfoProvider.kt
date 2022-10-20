package com.fingerprintjs.android.fingerprint.info_providers


import android.media.MediaCodecList
import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages
import com.fingerprintjs.android.fingerprint.tools.executeSafe


public data class MediaCodecInfo(
        val name: String,
        val capabilities: List<String>
)

@Deprecated(message = DeprecationMessages.UNREACHABLE_SYMBOL_UNINTENDED_PUBLIC_API)
public interface CodecInfoProvider {
    public fun codecsList(): List<MediaCodecInfo>
}

internal class CodecInfoProviderImpl(
        private val codecList: MediaCodecList
) : CodecInfoProvider {
    override fun codecsList(): List<MediaCodecInfo> {
        return executeSafe({ extractCodecInfo() }, emptyList())
    }

    private fun extractCodecInfo(): List<MediaCodecInfo> {
        return codecList.codecInfos.map {
            MediaCodecInfo(
                it.name,
                it.supportedTypes.toList()
            )
        }
    }
}