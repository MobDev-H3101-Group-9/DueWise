package com.duewise.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Our prototype only has a light design, so the app always uses light colors.
private val DueWiseColors = lightColorScheme(
    primary = AccentBlue,
    background = AppBackground,
    surface = CardSurface,
    onSurface = TextPrimary,
    error = DangerRed
)

@Composable
fun DueWiseTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DueWiseColors,
        typography = DueWiseTypography,
        content = content
    )
}
