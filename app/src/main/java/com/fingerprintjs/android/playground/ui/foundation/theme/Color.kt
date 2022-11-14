package com.fingerprintjs.android.playground.ui.foundation.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// We will use this color as a marker that we need to define some color explicitly and
// not use the default one
private val UndefinedColor = Color(0xFFFFEB3B)

private object PaletteTokens {
    val Neutral0 = Color(0xff0a0a0a)
    val Neutral40 = Color(0xff727273)
    val Neutral90 = Color(0xffe8e8e8)
    val Primary30 = Color(0xff80290A)
    val Primary50 = Color(0xffe56436)
    val Primary = Color(0xfffa7545)
    val Primary60 = Color(0xfffb8358)
    val Primary90 = Color(0xfffee3da)
    val Primary95 = Color(0xfffff1ec)
    val Primary99 = Color(0xfffff8f6)
    val Primary100 = Color(0xffffffff)
}

val LightMaterialColorScheme = lightColorScheme(
    primary = PaletteTokens.Primary,
    onPrimary = PaletteTokens.Primary100,
    primaryContainer = PaletteTokens.Primary60,
    onPrimaryContainer = PaletteTokens.Primary100,
    inversePrimary = UndefinedColor,
    secondary = UndefinedColor,
    onSecondary = UndefinedColor,
    secondaryContainer = UndefinedColor,
    onSecondaryContainer = UndefinedColor,
    tertiary = UndefinedColor,
    onTertiary = UndefinedColor,
    tertiaryContainer = UndefinedColor,
    onTertiaryContainer = UndefinedColor,
    background = PaletteTokens.Primary100,
    onBackground = PaletteTokens.Neutral0,
    surface = PaletteTokens.Primary99,
    onSurface = PaletteTokens.Neutral0,
    surfaceVariant = UndefinedColor,
    onSurfaceVariant = PaletteTokens.Neutral0,
    surfaceTint = PaletteTokens.Primary,
    inverseSurface = UndefinedColor,
    inverseOnSurface = UndefinedColor,
    error = UndefinedColor,
    onError = UndefinedColor,
    errorContainer = UndefinedColor,
    onErrorContainer = UndefinedColor,
    outline = UndefinedColor,
    outlineVariant = PaletteTokens.Neutral90,
    scrim = PaletteTokens.Neutral0,
)

@Stable
class ExtendedColorScheme(
    primaryDark: Color,
    onSurfaceHighlighted: Color,
    onSurfaceLight: Color,
    onBackgroundLight: Color,
) {
    var primaryDark by mutableStateOf(primaryDark, structuralEqualityPolicy())
        internal set
    var onSurfaceHighlighted by mutableStateOf(onSurfaceHighlighted, structuralEqualityPolicy())
        internal set
    var onSurfaceLight by mutableStateOf(onSurfaceLight, structuralEqualityPolicy())
        internal set
    var onBackgroundLight by mutableStateOf(onBackgroundLight, structuralEqualityPolicy())
        internal set
}

val LightExtendedColorScheme = ExtendedColorScheme(
    primaryDark = PaletteTokens.Primary50,
    onSurfaceHighlighted = PaletteTokens.Primary30,
    onSurfaceLight = PaletteTokens.Neutral40,
    onBackgroundLight = PaletteTokens.Neutral40,
)

@Preview
@Composable
private fun PreviewMaterialThemeColors() {
    AppTheme {
        Column(modifier = Modifier.width(220.dp)) {
            with(MaterialTheme.colorScheme) {
                listOf(
                    primary to "primary",
                    onPrimary to "onPrimary",
                    primaryContainer to "primaryContainer",
                    onPrimaryContainer to "onPrimaryContainer",
                    inversePrimary to "inversePrimary",
                    secondary to "secondary",
                    onSecondary to "onSecondary",
                    secondaryContainer to "secondaryContainer",
                    onSecondaryContainer to "onSecondaryContainer",
                    tertiary to "tertiary",
                    onTertiary to "onTertiary",
                    tertiaryContainer to "tertiaryContainer",
                    onTertiaryContainer to "onTertiaryContainer",
                    background to "background",
                    onBackground to "onBackground",
                    surface to "surface",
                    onSurface to "onSurface",
                    surfaceVariant to "surfaceVariant",
                    onSurfaceVariant to "onSurfaceVariant",
                    surfaceTint to "surfaceTint",
                    inverseSurface to "inverseSurface",
                    inverseOnSurface to "inverseOnSurface",
                    error to "error",
                    onError to "onError",
                    errorContainer to "errorContainer",
                    onErrorContainer to "onErrorContainer",
                    outline to "outline",
                    outlineVariant to "outlineVariant",
                    scrim to "scrim",
                ).forEach { (color, name) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "$name: ")
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(color = color)
                        )
                    }
                }
            }
        }
    }

}

@Preview
@Composable
private fun PreviewExtendedThemeColors() {
    AppTheme {
        Column(modifier = Modifier.width(220.dp)) {
            with(AppTheme.extendedColorScheme) {
                listOf(
                    primaryDark to "primaryDark",
                    onSurfaceHighlighted to "onSurfaceHighlighted",
                ).forEach { (color, name) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "$name: ")
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(color = color)
                        )
                    }
                }
            }
        }
    }

}
