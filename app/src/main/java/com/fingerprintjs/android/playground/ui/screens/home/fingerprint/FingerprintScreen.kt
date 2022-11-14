package com.fingerprintjs.android.playground.ui.screens.home.fingerprint

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
import com.fingerprintjs.android.playground.ui.foundation.InfoColumn
import com.fingerprintjs.android.playground.ui.foundation.ValuePicker
import com.fingerprintjs.android.playground.ui.foundation.theme.AppTheme
import com.fingerprintjs.android.playground.utils.mappers.description

@Preview
@Composable
fun PreviewFingerprintScreen() {
    AppTheme {
        FingerprintScreen(
            fingerprintState = FingerprintScreenState(
                stabilityLevel = StabilityLevel.STABLE,
                version = Fingerprinter.Version.V_5,
                fingerprintInfo = FingerprintScreenState.FingerprintInfoState.Ready(
                    fingerprint = "aio34534509fitd8",
                    signals = listOf(
                        FingerprintItemData.EXAMPLE,
                        FingerprintItemData.EXAMPLE,
                        FingerprintItemData.EXAMPLE,
                        FingerprintItemData.EXAMPLE,
                        FingerprintItemData.EXAMPLE,
                    ),
                ),
            ),
            onVersionChanged = {},
            onStabilityLevelChanged = {},
            onDeviceIdAndFingerprintDifferenceBannerClicked = {},
            onTryFingerprintProDemoBannerClicked = {},
        )
    }
}

@Composable
fun FingerprintScreen(
    fingerprintState: FingerprintScreenState,
    onVersionChanged: (Fingerprinter.Version) -> Unit,
    onStabilityLevelChanged: (StabilityLevel) -> Unit,
    onDeviceIdAndFingerprintDifferenceBannerClicked: () -> Unit,
    onTryFingerprintProDemoBannerClicked: () -> Unit,
) {
    InfoColumn(
        parameters = {
            ValuePicker(
                modifier = Modifier.weight(1f),
                title = "Version",
                values = Fingerprinter.Version.values().toList(),
                currentValue = fingerprintState.version,
                valueDescription = { this.description },
                onValueChanged = onVersionChanged,
            )

            Spacer(Modifier.width(20.dp))

            ValuePicker(
                modifier = Modifier.weight(1f),
                title = "Stability level",
                values = StabilityLevel.values().toList(),
                currentValue = fingerprintState.stabilityLevel,
                valueDescription = { this.description },
                onValueChanged = onStabilityLevelChanged,
            )
        },
        title = "Your Device Fingerprint",
        value = (fingerprintState.fingerprintInfo as? FingerprintScreenState.FingerprintInfoState.Ready)
            ?.fingerprint
            ?: "",
        signals = (fingerprintState.fingerprintInfo as? FingerprintScreenState.FingerprintInfoState.Ready)
            ?.signals
            ?: emptyList(),
        onDeviceIdAndFingerprintDifferenceBannerClicked = onDeviceIdAndFingerprintDifferenceBannerClicked,
        onTryFingerprintProDemoBannerClicked = onTryFingerprintProDemoBannerClicked,
        signalContent = { data, expanded, onExpand ->
            FingerprintSignalItem(data = data, isExpanded = expanded, onExpand = onExpand)
        }
    )
}
