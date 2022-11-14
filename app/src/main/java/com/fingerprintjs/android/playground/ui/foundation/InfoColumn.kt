package com.fingerprintjs.android.playground.ui.foundation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fingerprintjs.android.playground.R
import com.fingerprintjs.android.playground.ui.foundation.theme.AppTheme
import com.fingerprintjs.android.playground.utils.IgnoringLocalMinimumTouchTargetEnforcement
import kotlinx.coroutines.launch

@Preview
@Composable
private fun PreviewFingerprintScreen() {
    com.fingerprintjs.android.playground.ui.screens.home.fingerprint.PreviewFingerprintScreen()
}

@Preview(
    heightDp = 1000
)
@Composable
private fun PreviewDeviceIdScreen() {
    com.fingerprintjs.android.playground.ui.screens.home.device_id.PreviewDeviceIdScreen()
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun <T> InfoColumn(
    parameters: @Composable RowScope.() -> Unit,
    title: String,
    value: String,
    signals: List<T>,
    onDeviceIdAndFingerprintDifferenceBannerClicked: () -> Unit,
    onTryFingerprintProDemoBannerClicked: () -> Unit,
    signalContent: @Composable (signal: T, expanded: Boolean, onExpand: (Boolean) -> Unit) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val signalsToShow = remember(expanded, signals) {
        if (expanded) signals else signals.take(COLLAPSED_SIGNALS_MAX_LIST_SIZE)
    }
    val showShowMoreButton = remember(expanded, signals) {
        !expanded && signals.size > COLLAPSED_SIGNALS_MAX_LIST_SIZE
    }
    val showLessButton = remember(expanded, signals) {
        expanded && signals.size > COLLAPSED_SIGNALS_MAX_LIST_SIZE
    }
    val expandedIndexes = remember(signals) {
        mutableStateListOf(*signals.map { false }.toTypedArray())
    }

    val state = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val signalsFirstIndexInColumn = 6 // NOTE: don't forget to update this value when modifying the following LazyColumn
    LazyColumn(
        state = state,
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(
            all = 24.dp
        )
    ) {
        item {
            Row {
                parameters()
            }
        }

        item {
            Spacer(Modifier.height(24.dp))
        }

        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.W500,
            )
        }

        item {
            Spacer(Modifier.height(8.dp))
        }

        item {
            val transition = updateTransition(targetState = value, label = "AnimatedContent")
            transition.AnimatedContent(
                transitionSpec = {
                    if (transition.currentState.isNotEmpty()) {
                        fadeIn(animationSpec = tween(220, delayMillis = 90)) +
                                scaleIn() with
                                fadeOut(animationSpec = tween(90)) + scaleOut()
                    } else {
                        EnterTransition.None with ExitTransition.None
                    }
                }
            ) { state ->
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = state,
                    style = MaterialTheme.typography.headlineMedium,
                )
            }
        }

        item {
            Text(
                modifier = Modifier.padding(vertical = 24.dp),
                style = MaterialTheme.typography.labelMedium,
                color = AppTheme.extendedColorScheme.onBackgroundLight,
                text = "USED SIGNALS"
            )
        }

        if (signals.isNotEmpty()) {
            items(count = signalsToShow.size) { index ->
                signalContent(
                    signal = signalsToShow[index],
                    expanded = expandedIndexes[index],
                    onExpand = { expanded ->
                        expandedIndexes[index] = expanded
                        val itemGlobalIndex = index + signalsFirstIndexInColumn
                        if (!expanded && state.firstVisibleItemIndex == itemGlobalIndex) {
                            // If the item is being collapsed and is partially visible on top of our column,
                            // then scroll to it. This is helpful for large items.
                            scope.launch { state.animateScrollToItem(itemGlobalIndex) }
                        }
                    }
                )
                if (index != signalsToShow.lastIndex) {
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp)
                    )
                }
            }
            if (showShowMoreButton || showLessButton) {
                item {
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp)
                    )
                    IgnoringLocalMinimumTouchTargetEnforcement {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = AppTheme.extendedColorScheme.onSurfaceHighlighted,
                            ),
                            shape = MaterialTheme.shapes.medium,
                            contentPadding = PaddingValues(all = 12.dp),
                            onClick = { expanded = !expanded }
                        ) {
                            Text(
                                text = if (expanded) "Show Less" else "Show More",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.W500,
                            )
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(40.dp))
            }

            item {
                DeviceIdAndFingerprintDifferenceBanner(
                    onClick = onDeviceIdAndFingerprintDifferenceBannerClicked
                )
            }

            item {
                Spacer(Modifier.height(24.dp))
            }

            item {
                TryFingerprintProDemoBanner(
                    onClick = onTryFingerprintProDemoBannerClicked
                )
            }

            item {
                with(LocalDensity.current) {
                    Spacer(Modifier.height(WindowInsets.navigationBars.getBottom(this).toDp()))
                }
            }
        }
    }
}

@Composable
private fun DeviceIdAndFingerprintDifferenceBanner(
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick.invoke() },
        shape = MaterialTheme.shapes.medium,
        contentColor = AppTheme.extendedColorScheme.onSurfaceHighlighted,
    ) {
        Row(
            modifier = Modifier.padding(all = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.Outlined.Info,
                contentDescription = "Icon device ID and fingerprint difference"
            )
            Spacer(Modifier.width(10.dp))
            val arrowId = "[arrow]"
            val text = remember(Unit) {
                buildAnnotatedString {
                    append("Learn the difference between Device ID and Device Fingerprint ")
                    appendInlineContent(id = arrowId)
                }
            }
            val textStyle = MaterialTheme.typography.bodyMedium
            val inlineContent = mapOf(
                arrowId to InlineTextContent(
                    Placeholder(
                        width = textStyle.fontSize,
                        height = textStyle.fontSize,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowForward,
                        contentDescription = "Arrow forward",
                    )
                }
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.W500,
                inlineContent = inlineContent,
            )
        }
    }
}

@Composable
private fun TryFingerprintProDemoBanner(
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    ) {
        Box {
            Column(Modifier.padding(all = 16.dp)) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Get more accurate ID",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.headlineMedium,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Try Fingerprint Pro Demo",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.W500
                )
                Spacer(Modifier.height(24.dp))
                IgnoringLocalMinimumTouchTargetEnforcement {
                    Button(
                        onClick = onClick,
                        shape = MaterialTheme.shapes.small,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppTheme.extendedColorScheme.primaryDark,
                        ),
                        contentPadding = PaddingValues(horizontal = 32.dp, vertical = 11.dp)
                    ) {
                        Text(
                            text = "Try Now",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.W500,
                        )
                    }
                }
            }
            val painter = painterResource(R.drawable.ic_fingerprint_orange)
            val iconAspectRatio =
                painter.intrinsicSize.width /
                        painter.intrinsicSize.height
            Icon(
                modifier = Modifier
                    .height(120.dp)
                    .aspectRatio(iconAspectRatio)
                    .align(Alignment.BottomEnd)
                    .offset(x = 22.dp, y = 25.dp),
                painter = painter,
                contentDescription = "Fingerprint icon",
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

private const val COLLAPSED_SIGNALS_MAX_LIST_SIZE = 3
