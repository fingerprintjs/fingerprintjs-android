package com.fingerprintjs.android.playground.ui.screens.home.fingerprint

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
import com.fingerprintjs.android.playground.ui.foundation.SignalItem
import com.fingerprintjs.android.playground.ui.foundation.theme.AppTheme
import com.fingerprintjs.android.playground.utils.FP_SIGNALS_PREVIEW_LONG_VALUE
import com.fingerprintjs.android.playground.utils.mappers.description

@Preview
@Composable
fun PreviewFingerprintIdSignalItem() {
    var expanded by remember { mutableStateOf(false) }
    FingerprintSignalItem(
        FingerprintItemData.EXAMPLE,
        isExpanded = false,
        onExpand = { expanded = it },
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_3,
)
@Composable
fun PreviewInFingerprintScreen() {
    PreviewFingerprintScreen()
}

data class FingerprintItemData(
    val signalName: String,
    val signalValue: String,
    val stabilityLevel: StabilityLevel,
    val versionStart: Fingerprinter.Version,
    val versionEnd: Fingerprinter.Version
) {
    companion object {
        val EXAMPLE = FingerprintItemData(
            signalName = "Android ID",
            signalValue = FP_SIGNALS_PREVIEW_LONG_VALUE,
            stabilityLevel = StabilityLevel.STABLE,
            versionStart = Fingerprinter.Version.V_2,
            versionEnd = Fingerprinter.Version.V_5,
        )
    }
}

@Composable
fun FingerprintSignalItem(
    data: FingerprintItemData,
    isExpanded: Boolean,
    onExpand: (Boolean) -> Unit,
) {
    SignalItem(
        name = data.signalName,
        value = data.signalValue,
        isExpanded = isExpanded,
        onExpand = onExpand,
        tags = {
            TagItem(value = data.stabilityLevel.description, emphasized = true)
            Spacer(modifier = Modifier.width(8.dp))
            TagItem(value = "${data.versionStart.description} — ${data.versionEnd.description}", emphasized = false)
        }
    )
}

@Composable
private fun TagItem(
    value: String,
    emphasized: Boolean,
) {
    Surface(
        shape = CircleShape,
        tonalElevation = if (emphasized) 8.dp else 0.dp,
        contentColor = AppTheme.extendedColorScheme.onSurfaceHighlighted,
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.W600,
        )
    }
}
