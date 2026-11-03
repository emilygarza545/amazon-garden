package com.Bibhavu3.Mamzon.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val GardenColorScheme = darkColorScheme(
    primary = JungleGreen,
    onPrimary = WarmCream,
    primaryContainer = JungleGreenLight,
    onPrimaryContainer = WarmCream,
    secondary = SunlitGold,
    onSecondary = ShadowDeep,
    secondaryContainer = SurfaceRaised,
    onSecondaryContainer = SunlitGold,
    tertiary = LeafMint,
    onTertiary = ShadowDeep,
    background = DeepFoliage,
    onBackground = WarmCream,
    surface = SurfaceRaised,
    onSurface = WarmCream,
    surfaceVariant = SoilTile,
    onSurfaceVariant = SageText,
    outline = LeafMint,
    outlineVariant = MutedText,
    error = EmberOrange,
    onError = WarmCream,
    scrim = ShadowDeep
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = GardenColorScheme,
        typography = AppTypography,
        content = content
    )
}
