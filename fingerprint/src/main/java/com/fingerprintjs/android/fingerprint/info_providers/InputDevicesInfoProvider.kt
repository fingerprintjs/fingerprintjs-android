package com.fingerprintjs.android.fingerprint.info_providers


import android.hardware.input.InputManager
import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages
import com.fingerprintjs.android.fingerprint.tools.executeSafe


@Deprecated(message = DeprecationMessages.UNREACHABLE_SYMBOL_UNINTENDED_PUBLIC_API)
public interface InputDeviceDataSource {
    public fun getInputDeviceData(): List<InputDeviceData>
}

public data class InputDeviceData(
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