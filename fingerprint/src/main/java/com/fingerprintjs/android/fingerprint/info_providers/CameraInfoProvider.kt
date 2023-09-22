package com.fingerprintjs.android.fingerprint.info_providers


import android.hardware.Camera
import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages
import com.fingerprintjs.android.fingerprint.tools.safe.safe
import java.util.LinkedList


public data class CameraInfo(
    val cameraName: String,
    val cameraType: String,
    val cameraOrientation: String
)

@Deprecated(message = DeprecationMessages.UNREACHABLE_SYMBOL_UNINTENDED_PUBLIC_API)
public interface CameraInfoProvider {
    public fun getCameraInfo(): List<CameraInfo>
}

@Suppress("DEPRECATION")
internal class CameraInfoProviderImpl(
) : CameraInfoProvider {
    override fun getCameraInfo(): List<CameraInfo> {
        return safe {
            extractInfo()
        }.getOrDefault(emptyList())
    }

    private fun extractInfo(): List<CameraInfo> {
        // There is a report that the following call might hang forever (it's handled now by [safe] function).
        // https://github.com/fingerprintjs/fingerprintjs-android/issues/35
        // Normally it takes around 1-5ms to get the number of cameras.
        val numberOfCameras = Camera.getNumberOfCameras()
        val result = LinkedList<CameraInfo>()

        for (i in 0 until numberOfCameras) {
            val info = Camera.CameraInfo()
            Camera.getCameraInfo(i, info)
            val type = stringDescriptionForType(info.facing)
            val orientation = info.orientation.toString()
            result.add(
                CameraInfo(
                    i.toString(),
                    type,
                    orientation
                )
            )
        }

        return result
    }

    private fun stringDescriptionForType(type: Int) = when (type) {
        Camera.CameraInfo.CAMERA_FACING_FRONT -> "front"
        Camera.CameraInfo.CAMERA_FACING_BACK -> "back"
        else -> ""
    }
}
