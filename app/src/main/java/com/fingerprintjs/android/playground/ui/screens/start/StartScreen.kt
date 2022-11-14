package com.fingerprintjs.android.playground.ui.screens.start

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fingerprintjs.android.playground.R
import com.fingerprintjs.android.playground.ui.foundation.theme.AppTheme
import com.fingerprintjs.android.playground.utils.addCenteredVerticallyInRootPadding

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_3,
)
@Composable
private fun PreviewMainDrawer() {
    AppTheme {
        Box(contentAlignment = Alignment.Center) {
            StartScreenContent(onNext = {})
        }
    }
}

@Composable
fun StartScreen(onNext: () -> Unit) {
    var screenGlobalCoordinates by remember {
        mutableStateOf<LayoutCoordinates?>(null)
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .onGloballyPositioned { coordinates ->
            screenGlobalCoordinates = coordinates
        }
        .addCenteredVerticallyInRootPadding(screenGlobalCoordinates)) {
        screenGlobalCoordinates?.let {
            StartScreenContent(onNext = onNext)
        }
    }
}

@Composable
private fun BoxScope.StartScreenContent(onNext: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .align(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_fingerprint_orange),
            contentDescription = "Fingerprint icon",
            tint = MaterialTheme.colorScheme.primary,
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = "Identify my device",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "This Demo uses fingerprint-android open source library to showcase a lot of ways for device identification.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(40.dp))
        Button(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(66.dp),
            onClick = { onNext.invoke() },
            shape = MaterialTheme.shapes.large,
        ) {
            Text(
                text = "Get Started",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.W600,
            )
        }
    }
}

