package com.fingerprintjs.android.playground.ui.foundation.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

object AppTheme {
    val extendedColorScheme: ExtendedColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalExtendedColorScheme.current
}

private val LocalExtendedColorScheme = staticCompositionLocalOf { LightExtendedColorScheme }

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    val materialColorScheme = LightMaterialColorScheme
    val extendedColorScheme = LightExtendedColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context as Activity).window
        SideEffect {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowCompat.setDecorFitsSystemWindows(window, false)
                window.statusBarColor = Color.Transparent.toArgb()
                window.navigationBarColor = Color.Transparent.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    window.isStatusBarContrastEnforced = false
                    window.isNavigationBarContrastEnforced = true
                }
            } else {
                window.statusBarColor = materialColorScheme.onBackground.toArgb()
            }
        }
    }

    CompositionLocalProvider(
        LocalExtendedColorScheme provides extendedColorScheme
    ) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            typography = Typography,
            content = content
        )
    }
}
