@file:OptIn(ExperimentalMaterial3Api::class)

package com.fingerprintjs.android.playground.ui.screens.main.nav_drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fingerprintjs.android.playground.R
import com.fingerprintjs.android.playground.ui.foundation.theme.AppTheme

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_3,
)
@Composable
private fun PreviewMainDrawer() {
    AppTheme {
        MainDrawer(
            drawerState = rememberDrawerState(initialValue = DrawerValue.Open),
            gesturesEnabled = true,
            onReportIssueClicked = {},
            onAppSourceCodeClicked = {},
            onTryFpProDemoClicked = {},
            onFpProAccuracyClicked = {},
            onGithubClicked = {},
            onLinkedinClicked = {},
            onTwitterClicked = {},
            onFingerprintComClicked = {},
        ) {

        }
    }
}

@Composable
fun MainDrawer(
    drawerState: DrawerState,
    gesturesEnabled: Boolean,
    onReportIssueClicked: () -> Unit,
    onAppSourceCodeClicked: () -> Unit,
    onTryFpProDemoClicked: () -> Unit,
    onFpProAccuracyClicked: () -> Unit,
    onGithubClicked: () -> Unit,
    onLinkedinClicked: () -> Unit,
    onTwitterClicked: () -> Unit,
    onFingerprintComClicked: () -> Unit,
    mainContent: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        gesturesEnabled = gesturesEnabled,
        drawerContent = {
            ModalDrawerSheet(
                drawerTonalElevation = 0.dp,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(
                            start = 12.dp,
                            top = 56.dp,
                            end = 12.dp,
                            bottom = 40.dp
                        ),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Headline()
                        SectionHeader("Fingerprint OSS")
                        SectionItem(
                            name = "Report an Issue",
                            icon = { Icon(Icons.Outlined.BugReport, "Report an issue") },
                            onClick = onReportIssueClicked,
                        )
                        SectionItem(
                            name = "Source Code",
                            icon = { Icon(Icons.Outlined.Code, "Source code") },
                            onClick = onAppSourceCodeClicked,
                        )

                        SectionHeader("Fingerprint Pro Demo")
                        SectionItem(
                            name = "Try Fingerprint Pro Demo",
                            icon = { Icon(Icons.Outlined.Fingerprint, "Try Fingerprint Pro Demo") },
                            onClick = onTryFpProDemoClicked,
                        )
                        SectionItem(
                            name = "Fingerprint Pro Accuracy",
                            icon = { Icon(Icons.Outlined.Radar, "Fingerprint Pro Accuracy") },
                            onClick = onFpProAccuracyClicked,
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 32.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Row {
                            BottomIconedLink(
                                icon = { Icon(painter = painterResource(R.drawable.ic_github), contentDescription = "Github link") },
                                onClick = onGithubClicked,
                            )
                            Spacer(Modifier.width(24.dp))
                            BottomIconedLink(
                                icon = { Icon(painter = painterResource(R.drawable.ic_linkedin), contentDescription = "Linkedin link") },
                                onClick = onLinkedinClicked,
                            )
                            Spacer(Modifier.width(24.dp))
                            BottomIconedLink(
                                icon = { Icon(painter = painterResource(R.drawable.ic_twitter), contentDescription = "Twitter link") },
                                onClick = onTwitterClicked,
                            )
                        }
                        FingerprintLink(onClick = onFingerprintComClicked)
                    }
                }
            }
        },
        drawerState = drawerState,
    ) {
        mainContent()
    }
}

@Composable
private fun Headline() {
    Box(
        modifier = Modifier
            .height(56.dp)
            .padding(horizontal = 16.dp)
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterStart),
            text = "Fingerprint",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.W600,
        )
    }
}

@Composable
private fun SectionHeader(
    header: String,
) {
    Box(
        modifier = Modifier
            .height(56.dp)
            .padding(horizontal = 16.dp)
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterStart),
            text = header,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.W500,
            color = AppTheme.extendedColorScheme.onSurfaceLight,
        )
    }
}

@Composable
private fun SectionItem(
    name: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    NavigationDrawerItem(
        label = { Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.W500,
        ) },
        selected = false,
        onClick = onClick,
        icon = icon,
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
            selectedIconColor = MaterialTheme.colorScheme.onSurface,
            selectedTextColor = MaterialTheme.colorScheme.onSurface,
        )
    )
}

@Composable
private fun BottomIconedLink(
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    Box(modifier = Modifier
        .clip(MaterialTheme.shapes.extraSmall)
        .clickable { onClick.invoke() }) {
        icon()
    }
}

@Composable
private fun FingerprintLink(
    onClick: () -> Unit,
) {
    Text(
        modifier = Modifier
            .clip(MaterialTheme.shapes.extraSmall)
            .clickable { onClick.invoke() },
        text = "fingerprint.com",
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.W500,
    )
}
