package com.fingerprintjs.android.playground.ui.screens.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.ui.graphics.vector.ImageVector

enum class HomeScreenTab(
    val iconImageVector: () -> ImageVector,
    val iconContentDescription: String,
    val title: String,
) {
    DEVICE_ID(
        iconImageVector = { Icons.Filled.PhoneAndroid },
        iconContentDescription = "Device ID icon",
        title = "Device ID"
    ),
    FINGERPRINT(
        iconImageVector = { Icons.Filled.Menu },
        iconContentDescription = "Fingerprint icon",
        title = "Fingerprint"
    );
}
