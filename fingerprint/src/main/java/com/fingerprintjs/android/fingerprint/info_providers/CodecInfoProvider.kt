package com.fingerprintjs.android.fingerprint.info_providers


import android.media.MediaCodecList
import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages
import com.fingerprintjs.android.fingerprint.tools.safe.SafeLazy
import com.fingerprintjs.android.fingerprint.tools.safe.safe


public data class MediaCodecInfo(
        val name: String,
        val capabilities: List<String> // theoretically, may contain "null" strings for backwards compatibility reasons
)

@Deprecated(message = DeprecationMessages.UNREACHABLE_SYMBOL_UNINTENDED_PUBLIC_API)
public interface CodecInfoProvider {
    public fun codecsList(): List<MediaCodecInfo>
}

internal class CodecInfoProviderImpl(
    private val codecList: SafeLazy<MediaCodecList>,
) : CodecInfoProvider {
    override fun codecsList(): List<MediaCodecInfo> {
        return safe {
            extractCodecInfo()
        }.getOrDefault(emptyList())
    }

    private fun extractCodecInfo(): List<MediaCodecInfo> {
        return codecList.getOrThrow().codecInfos.map {
            MediaCodecInfo(
                it!!.name!!,
                it.supportedTypes!!.map { type: String? -> type.toString() },
            )
        }
    }
}