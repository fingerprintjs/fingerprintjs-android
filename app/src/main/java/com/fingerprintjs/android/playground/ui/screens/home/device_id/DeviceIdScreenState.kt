package com.fingerprintjs.android.playground.ui.screens.home.device_id

import com.fingerprintjs.android.fingerprint.Fingerprinter

data class DeviceIdScreenState(
    val version: Fingerprinter.Version,
    val deviceIdInfo: DeviceIdInfoState,
) {
    sealed class DeviceIdInfoState() {
        object Initializing : DeviceIdInfoState()
        class Ready(
            val deviceId: String,
            val signals: List<DeviceIdSignalItemData>,
        ) : DeviceIdInfoState()
    }
}