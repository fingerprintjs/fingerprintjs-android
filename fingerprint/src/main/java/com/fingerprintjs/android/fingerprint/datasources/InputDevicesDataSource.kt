package com.fingerprintjs.android.fingerprint.datasources

import android.hardware.input.InputManager
import android.os.Build
import com.fingerprintjs.android.fingerprint.tools.executeSafe


interface InputDeviceDataSource {
    fun getInputDeviceData(): List<InputDeviceData>
}

data class InputDeviceData(
    val name: String,
    val vendor: String
)

class InputDevicesDataSourceImpl(
    private val inputDeviceManager: InputManager
) : InputDeviceDataSource {
    override fun getInputDeviceData(): List<InputDeviceData> {
        return executeSafe(
            {
                inputDeviceManager.inputDeviceIds.map {
                    val inputDevice = inputDeviceManager.getInputDevice(it)
                    val vendorId = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                        ""
                    } else inputDevice.vendorId.toString()
                    InputDeviceData(
                        inputDevice.name,
                        vendorId
                    )
                }
            }, emptyList()
        )
    }
}