package com.fingerprintjs.android.playground.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
import com.fingerprintjs.android.playground.di.getAppComponent
import com.fingerprintjs.android.playground.ui.foundation.theme.AppTheme
import com.fingerprintjs.android.playground.ui.screens.home.device_id.DeviceIdScreen
import com.fingerprintjs.android.playground.ui.screens.home.fingerprint.FingerprintScreen
import com.fingerprintjs.android.playground.utils.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_3,
)
@Composable
fun HomeScreenPreview() {
    AppTheme {
        HomeScreenInternal(
            state = HomeScreenState.Preview,
            onDeviceIdVersionChanged = {},
            onFingerprintVersionChanged = {},
            onFingerprintStabilityLevelChanged = {},
            onDeviceIdAndFingerprintDifferenceBannerClicked = {},
            onTryFingerprintProDemoBannerClicked = {},
        )
    }
}

@Composable
fun HomeScreen(
    onDeviceIdAndFingerprintDifferenceBannerClicked: () -> Unit,
    onTryFingerprintProDemoBannerClicked: () -> Unit,
) {
    val appComponent = getAppComponent()
    val viewModel = viewModel {
        appComponent.homeViewModel
    }
    val state by viewModel.homeScreenState.collectAsState()
    state?.let {
        HomeScreenInternal(
            state = it,
            onDeviceIdVersionChanged = viewModel::onDeviceIdVersionChanged,
            onFingerprintVersionChanged = viewModel::onFingerprintVersionChanged,
            onFingerprintStabilityLevelChanged = viewModel::onFingerprintStabilityLevelChanged,
            onDeviceIdAndFingerprintDifferenceBannerClicked = onDeviceIdAndFingerprintDifferenceBannerClicked,
            onTryFingerprintProDemoBannerClicked = onTryFingerprintProDemoBannerClicked,
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun HomeScreenInternal(
    state: HomeScreenState,
    onDeviceIdVersionChanged: (newVersion: Fingerprinter.Version) -> Unit,
    onFingerprintVersionChanged: (newVersion: Fingerprinter.Version) -> Unit,
    onFingerprintStabilityLevelChanged: (newStabilityLevel: StabilityLevel) -> Unit,
    onDeviceIdAndFingerprintDifferenceBannerClicked: () -> Unit,
    onTryFingerprintProDemoBannerClicked: () -> Unit,
) {
    val pagerState = rememberPagerState(initialPage = 0)

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TabRow(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            selectedTabIndex = pagerState.currentPage,
        ) {
            HomeScreenTab.values().forEachIndexed { index, tab ->
                Tab(
                    text = {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = tab.title,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.W500,
                            )
                        }
                    },
                    icon = {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            tab.icon(
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .size(20.dp)
                            )
                        }
                    },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                )

            }
        }

        HorizontalPager(
            count = HomeScreenTab.values().size,
            state = pagerState,
        ) { page ->
            when (page) {
                HomeScreenTab.DEVICE_ID.ordinal -> {
                    DeviceIdScreen(
                        deviceIdScreenState = state.deviceIdScreenState,
                        onVersionChanged = onDeviceIdVersionChanged,
                        onDeviceIdAndFingerprintDifferenceBannerClicked = onDeviceIdAndFingerprintDifferenceBannerClicked,
                        onTryFingerprintProDemoBannerClicked = onTryFingerprintProDemoBannerClicked,
                    )
                }
                else -> {
                    FingerprintScreen(
                        fingerprintState = state.fingerprintScreenState,
                        onVersionChanged = onFingerprintVersionChanged,
                        onStabilityLevelChanged = onFingerprintStabilityLevelChanged,
                        onDeviceIdAndFingerprintDifferenceBannerClicked = onDeviceIdAndFingerprintDifferenceBannerClicked,
                        onTryFingerprintProDemoBannerClicked = onTryFingerprintProDemoBannerClicked
                    )
                }
            }
        }
    }
}
