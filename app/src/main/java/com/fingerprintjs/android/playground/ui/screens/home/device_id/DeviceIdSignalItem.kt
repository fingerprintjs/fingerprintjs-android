package com.fingerprintjs.android.playground.ui.screens.home.device_id

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.fingerprintjs.android.playground.ui.foundation.SignalItem
import com.fingerprintjs.android.playground.ui.foundation.theme.AppTheme

@Preview
@Composable
fun PreviewDeviceIdSignalItem() {
    AppTheme {
        DeviceIdSignalItem(
            DeviceIdSignalItemData.EXAMPLE
        )
    }
}

data class DeviceIdSignalItemData(
    val signalName: String,
    val signalValue: String,
) {
    companion object {
        val EXAMPLE = DeviceIdSignalItemData(
            signalName = "Android ID",
            signalValue = "aio34534509fitd8",
        )
    }
}

@Composable
fun DeviceIdSignalItem(
    data: DeviceIdSignalItemData
) {
    SignalItem(
        name = data.signalName,
        value = data.signalValue,
        isExpanded = true,
        onExpand = {},
        tags = null,
    )
}
