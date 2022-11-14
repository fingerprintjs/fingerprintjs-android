package com.fingerprintjs.android.playground.ui.screens.home.device_id

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.playground.ui.foundation.InfoColumn
import com.fingerprintjs.android.playground.ui.foundation.ValuePicker
import com.fingerprintjs.android.playground.ui.foundation.theme.AppTheme
import com.fingerprintjs.android.playground.utils.mappers.description

@Preview
@Composable
fun PreviewDeviceIdScreen() {
    AppTheme {
        DeviceIdScreen(
            deviceIdScreenState = DeviceIdScreenState(
                version = Fingerprinter.Version.V_5,
                deviceIdInfo = DeviceIdScreenState.DeviceIdInfoState.Ready(
                    deviceId = "aio34534509fitd8",
                    signals = listOf(
                        DeviceIdSignalItemData.EXAMPLE,
                        DeviceIdSignalItemData.EXAMPLE,
                        DeviceIdSignalItemData.EXAMPLE,
                        DeviceIdSignalItemData.EXAMPLE,
                        DeviceIdSignalItemData.EXAMPLE,
                    ),
                ),
            ),
            onVersionChanged = {},
            onDeviceIdAndFingerprintDifferenceBannerClicked = {},
            onTryFingerprintProDemoBannerClicked = {},
        )
    }
}

@Composable
fun DeviceIdScreen(
    deviceIdScreenState: DeviceIdScreenState,
    onVersionChanged: (Fingerprinter.Version) -> Unit,
    onDeviceIdAndFingerprintDifferenceBannerClicked: () -> Unit,
    onTryFingerprintProDemoBannerClicked: () -> Unit,
) {
    InfoColumn(
        parameters = {
            ValuePicker(
                modifier = Modifier.weight(1f),
                title = "Version",
                values = Fingerprinter.Version.values().toList(),
                currentValue = deviceIdScreenState.version,
                valueDescription = { this.description },
                onValueChanged = onVersionChanged,
            )
        },
        title = "Your Device ID",
        value = deviceIdScreenState
            .deviceIdInfo
            .let { it as? DeviceIdScreenState.DeviceIdInfoState.Ready }
            ?.deviceId
            ?: "",
        signals = deviceIdScreenState
            .deviceIdInfo
            .let { it as? DeviceIdScreenState.DeviceIdInfoState.Ready }
            ?.signals
            ?: emptyList(),
        onDeviceIdAndFingerprintDifferenceBannerClicked = onDeviceIdAndFingerprintDifferenceBannerClicked,
        onTryFingerprintProDemoBannerClicked = onTryFingerprintProDemoBannerClicked,
        signalContent = { signal: DeviceIdSignalItemData, _: Boolean, _: (Boolean) -> Unit ->
            DeviceIdSignalItem(data = signal)
        }
    )
}
