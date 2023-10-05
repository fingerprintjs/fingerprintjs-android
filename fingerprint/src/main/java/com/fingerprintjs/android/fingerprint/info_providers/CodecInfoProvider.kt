package com.fingerprintjs.android.fingerprint.info_providers


import android.media.MediaCodecList
import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages
import com.fingerprintjs.android.fingerprint.tools.threading.safe.safeWithTimeout


public data class MediaCodecInfo(
        val name: String,
        val capabilities: List<String> // theoretically, may contain "null" strings for backwards compatibility reasons
)

@Deprecated(message = DeprecationMessages.UNREACHABLE_SYMBOL_UNINTENDED_PUBLIC_API)
public interface CodecInfoProvider {
    public fun codecsList(): List<MediaCodecInfo>
}

internal class CodecInfoProviderImpl(
    private val codecList: MediaCodecList?,
) : CodecInfoProvider {
    override fun codecsList(): List<MediaCodecInfo> {
        return safeWithTimeout {
            extractCodecInfo()
        }.getOrDefault(emptyList())
    }

    private fun extractCodecInfo(): List<MediaCodecInfo> {
        return codecList!!.codecInfos.map {
            MediaCodecInfo(
                it!!.name!!,
                it.supportedTypes!!.map { type: String? -> type.toString() },
            )
        }
    }
}