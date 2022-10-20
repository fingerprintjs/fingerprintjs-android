package com.fingerprintjs.android.fingerprint.info_providers


import android.hardware.Camera
import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages
import com.fingerprintjs.android.fingerprint.tools.await
import com.fingerprintjs.android.fingerprint.tools.executeSafe
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
        return executeSafe({ extractInfo() }, emptyList())
    }

    private fun extractInfo(): List<CameraInfo> {
        // Avoiding thread hanging forever. Workaround for:
        // https://github.com/fingerprintjs/fingerprintjs-android/issues/35
        // Normally it takes around 1-5ms to get the number of cameras.
        val numberOfCameras = await(timeoutMillis = 1000) {
            Camera.getNumberOfCameras()
        }.getOrDefault(0)
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
