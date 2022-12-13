package com.fingerprintjs.android.playground.ui.screens.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.fingerprintjs.android.playground.R


enum class HomeScreenTab(
    val icon: @Composable (modifier: Modifier) -> Unit,
    val title: String,
) {
    DEVICE_ID(
        icon = { modifier ->
            Icon(
                modifier = modifier,
                imageVector = Icons.Filled.PhoneAndroid,
                contentDescription = "Device ID icon"
            )
        },
        title = "Device ID",
    ),
    FINGERPRINT(
        icon = { modifier ->
            Icon(
                modifier = modifier,
                painter = painterResource(R.drawable.ic_fingerprint_orange),
                contentDescription = "Fingerprint icon"
            )
        },
        title = "Device Fingerprint",
    );
}
