package com.fingerprintjs.android.playground.utils

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumTouchTargetEnforcement
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Add the paddings in such a way that the composable has equal space to the top and to the bottom relative to the
 * center of compose root view
 */
fun Modifier.addCenteredVerticallyInRootPadding(screenGlobalCoordinates: LayoutCoordinates?): Modifier = this.composed {
    if (screenGlobalCoordinates == null)
        return@composed this

    val topNeededOffset: Dp =
        (screenOffsetFromBottom(screenGlobalCoordinates) -
                screenOffsetFromTop(screenGlobalCoordinates))
            .takeIf { it > 0.dp } ?: 0.dp

    val bottomNeededOffset: Dp =
        (screenOffsetFromTop(screenGlobalCoordinates) -
                screenOffsetFromBottom(screenGlobalCoordinates))
            .takeIf { it > 0.dp } ?: 0.dp

    padding(
        top = topNeededOffset,
        bottom = bottomNeededOffset,
    )
}

@Composable
private fun screenOffsetFromTop(screenGlobalCoordinates: LayoutCoordinates): Dp {
    return LocalDensity.current.run {
        screenGlobalCoordinates.positionInRoot().y.toDp()
    }
}

@Composable
private fun screenOffsetFromBottom(screenGlobalCoordinates: LayoutCoordinates): Dp {
    return LocalConfiguration.current.screenHeightDp.dp -
            screenOffsetFromTop(screenGlobalCoordinates) -
            LocalDensity.current.run {
                screenGlobalCoordinates.size.height.toDp()
            }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IgnoringLocalMinimumTouchTargetEnforcement(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
        content()
    }
}

/**
 * Useful to mimic expanded/collapsed behaviour of arrow icons
 */
fun Modifier.animatedRotation(
    rotated: Boolean
): Modifier = composed {

    fun toDegrees(rotated: Boolean): Float {
        return if (rotated) 180f else 0f
    }

    var rotation by remember { mutableStateOf(toDegrees(rotated)) }
    LaunchedEffect(rotated) {
        animate(
            initialValue = rotation,
            targetValue = toDegrees(rotated),
            animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
        ) { value, _ -> rotation = value }
    }

    return@composed Modifier.rotate(rotation)
}
