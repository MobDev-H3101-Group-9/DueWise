package com.duewise.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DueWiseColors = lightColorScheme(
    primary = AccentBlue,
    onPrimary = CardSurface,
    secondary = PrimaryDark,
    onSecondary = CardSurface,
    background = AppBackground,
    onBackground = TextPrimary,
    surface = CardSurface,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    outline = CardBorder,
    error = DangerRed
)

/** The prototype is a light design, so the app always uses the light color scheme. */
@Composable
fun DueWiseTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DueWiseColors,
        typography = DueWiseTypography,
        content = content
    )
}
