@file:OptIn(ExperimentalMaterial3Api::class)

package com.fingerprintjs.android.playground.ui.screens.main

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.fingerprintjs.android.playground.di.getAppComponent
import com.fingerprintjs.android.playground.ui.screens.home.HomeScreen
import com.fingerprintjs.android.playground.ui.screens.main.nav_drawer.MainDrawer
import com.fingerprintjs.android.playground.ui.screens.start.StartScreen
import com.fingerprintjs.android.playground.utils.IntentUtils
import com.fingerprintjs.android.playground.utils.viewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_3,
)
@Composable
fun MainContentPreview() {
    MaterialTheme {
        MainContentInternal()
    }
}

@Composable
fun MainContent() {
    MainContentInternal()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainContentInternal() {
    val uiState = rememberMainContentState()
    val coroutineScope = rememberCoroutineScope()
    val activity = (LocalContext.current as? Activity)

    val appComponent = getAppComponent()
    val viewModel = viewModel { appComponent.mainViewModel }

    LaunchedEffect(Unit) {
        viewModel.externalLinkToOpen
            .onEach { link ->
                activity?.let { activity ->
                    IntentUtils.openUrl(activity, link)
                }
            }
            .launchIn(this)

        viewModel.sendReportEvent
            .onEach { reportAttachmentPath ->
                activity?.let { activity ->
                    if (reportAttachmentPath == null) {
                        Toast.makeText(
                            activity,
                            "Failed to attach report file",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    IntentUtils.sendReport(activity, reportAttachmentPath)
                }
            }
            .launchIn(this)
    }

    @Composable
    fun BackHandlerRespectingNavDrawer() {
        val isDrawerOpen = uiState.drawerState.isOpen
        BackHandler(enabled = isDrawerOpen) {
            coroutineScope.launch { uiState.drawerState.close() }
        }
        uiState.navController.enableOnBackPressed(enabled = !isDrawerOpen)
    }

    BackHandlerRespectingNavDrawer()

    MainDrawer(
        drawerState = uiState.drawerState,
        gesturesEnabled = uiState.shouldShowNavDrawer(),
        onReportIssueClicked = viewModel::onReportIssueClicked,
        onAppSourceCodeClicked = viewModel::onAppSourceCodeClicked,
        onTryFpProDemoClicked = viewModel::onTryFpProDemoClicked,
        onFpProAccuracyClicked = viewModel::onFpProAccuracyClicked,
        onGithubClicked = viewModel::onGithubClicked,
        onLinkedinClicked = viewModel::onLinkedinClicked,
        onTwitterClicked = viewModel::onTwitterClicked,
        onFingerprintComClicked = viewModel::onFingerprintComClicked,
    ) {
        Scaffold(
            contentWindowInsets = WindowInsets(
                bottom = 0.dp,
            ),
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                    ),
                    title = {
                        Text(
                            text = "Fingerprint",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.W600,
                        )
                    },
                    navigationIcon = {
                        AnimatedVisibility(
                            visible = uiState.shouldShowNavDrawer(),
                            enter = expandIn(
                                expandFrom = Alignment.CenterStart
                            )
                        ) {
                            IconButton(onClick = {
                                coroutineScope.launch { uiState.drawerState.open() }
                            }) {
                                Icon(imageVector = Icons.Filled.Menu, contentDescription = "Open Navigation Drawer")
                            }
                        }
                    },
                )
            },
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            ) {
                NavHost(
                    navController = uiState.navController,
                    startDestination = NavSections.START.route,
                ) {
                    composable(route = NavSections.START.route) {
                        StartScreen(onNext = uiState::navigateFromStartToHomeScreen)
                    }
                    composable(route = NavSections.HOME.route) {
                        HomeScreen(
                            onDeviceIdAndFingerprintDifferenceBannerClicked = viewModel::onDeviceIdAndFingerprintDifferenceBannerClicked,
                            onTryFingerprintProDemoBannerClicked = viewModel::onTryFingerprintProDemoBannerClicked,
                        )
                    }
                }
            }
        }
    }
}
