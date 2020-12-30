package com.fingerprintjs.android.fingerprint.datasources


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

class CameraInfoProviderImpl(
) : CameraInfoProvider {
    override fun getCameraInfo(): List<CameraInfo> {
        return executeSafe({ extractInfo() }, emptyList())
    }

    @Suppress("DEPRECATION")
    private fun extractInfo(): List<CameraInfo> {
        val numbersOfCameras = Camera.getNumberOfCameras()
        val result = LinkedList<CameraInfo>()

        for (i in 0 until numbersOfCameras) {
            val info = Camera.CameraInfo()
            Camera.getCameraInfo(i, info)
            val type = info.facing.toString()
            val orientation = info.orientation.toString()
            result.add(CameraInfo(i.toString(), type, orientation))
        }

        return result
    }
}
