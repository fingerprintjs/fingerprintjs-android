package com.fingerprintjs.android.fingerprint.info_providers


import android.hardware.input.InputManager
import com.fingerprintjs.android.fingerprint.tools.executeSafe


interface InputDeviceDataSource {
    fun getInputDeviceData(): List<InputDeviceData>
}

data class InputDeviceData(
    val name: String,
    val vendor: String
)

internal class InputDevicesDataSourceImpl(
    private val inputDeviceManager: InputManager
) : InputDeviceDataSource {
    override fun getInputDeviceData(): List<InputDeviceData> {
        return executeSafe(
            {
                inputDeviceManager.inputDeviceIds.map {
                    val inputDevice = inputDeviceManager.getInputDevice(it)
                    val vendorId = inputDevice.vendorId.toString()
                    InputDeviceData(
                        inputDevice.name,
                        vendorId
                    )
                }
            }, emptyList()
        )
    }
}