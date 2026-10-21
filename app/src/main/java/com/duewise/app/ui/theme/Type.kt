package com.duewise.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// One set of text sizes for the whole app so all screens look the same.
val DueWiseTypography = Typography(
    headlineMedium = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.SemiBold), // screen titles
    titleLarge = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Normal),       // task / assignment name on details screens
    titleMedium = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium),      // card titles
    bodyLarge = TextStyle(fontSize = 16.sp),                                        // normal text
    bodyMedium = TextStyle(fontSize = 14.sp),                                       // smaller gray text
    labelMedium = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold),    // section labels like "UPCOMING TASKS"
    labelLarge = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)        // button text
)
