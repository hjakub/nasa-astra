package com.example.ad_astra.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val BlueDarkColorScheme = darkColorScheme(
    primary = BlueAccent,
    onPrimary = White,

    secondary = BlueDarkVariant,
    onSecondary = White,

    background = BlueDark,
    onBackground = White,

    surface = BlueDark,
    onSurface = White,

    surfaceVariant = BlueDarkVariant,
    onSurfaceVariant = White,

    outline = White70
)

@Composable
fun AdAstraTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = BlueDarkColorScheme,
        typography = Typography(),
        content = content
    )
}
