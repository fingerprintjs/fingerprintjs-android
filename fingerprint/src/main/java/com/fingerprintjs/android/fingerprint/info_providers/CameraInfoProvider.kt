package com.fingerprintjs.android.fingerprint.info_providers


import android.hardware.Camera
import com.fingerprintjs.android.fingerprint.tools.executeSafe
import java.util.LinkedList


data class CameraInfo(
    val cameraName: String,
    val cameraType: String,
    val cameraOrientation: String
)

interface CameraInfoProvider {
    fun getCameraInfo(): List<CameraInfo>
}

@Suppress("DEPRECATION")
internal class CameraInfoProviderImpl(
) : CameraInfoProvider {
    override fun getCameraInfo(): List<CameraInfo> {
        return executeSafe({ extractInfo() }, emptyList())
    }

    private fun extractInfo(): List<CameraInfo> {
        val numbersOfCameras = Camera.getNumberOfCameras()
        val result = LinkedList<CameraInfo>()

        for (i in 0 until numbersOfCameras) {
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
