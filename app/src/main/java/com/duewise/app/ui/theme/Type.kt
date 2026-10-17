package com.duewise.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * One type scale for the whole app, so every screen uses the same sizes.
 * - headlineMedium: screen titles ("Upcoming", "Task details")
 * - titleLarge: the main subject of a details screen (task or assignment name)
 * - titleMedium: card titles
 * - bodyLarge / bodyMedium: regular and secondary text
 * - labelMedium: uppercase section labels ("UPCOMING TASKS")
 * - labelLarge: button text
 */
val DueWiseTypography = Typography(
    headlineMedium = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.SemiBold, lineHeight = 36.sp),
    titleLarge = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Normal, lineHeight = 32.sp),
    titleMedium = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium, lineHeight = 24.sp),
    bodyLarge = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal, lineHeight = 22.sp),
    bodyMedium = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal, lineHeight = 20.sp),
    labelMedium = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.6.sp),
    labelLarge = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
)
