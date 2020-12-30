package com.fingerprintjs.android.fingerprint.datasources


import android.media.MediaCodecList
import android.os.Build
import com.fingerprintjs.android.fingerprint.tools.executeSafe


data class MediaCodecInfo(
        val name: String,
        val capabilities: List<String>
)

interface CodecInfoProvider {
    fun codecsList(): List<MediaCodecInfo>
}

class CodecInfoProviderImpl(
        private val codecList: MediaCodecList
) : CodecInfoProvider {
    override fun codecsList(): List<MediaCodecInfo> {
        return executeSafe({ extractCodecInfo() }, emptyList())
    }

    private fun extractCodecInfo(): List<MediaCodecInfo> {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return emptyList()
        }

        return codecList.codecInfos.map {
            MediaCodecInfo(
                    it.name,
                    it.supportedTypes.toList()
            )
        }
    }
}