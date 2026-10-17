package com.duewise.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.duewise.app.ui.theme.AccentBlue
import com.duewise.app.ui.theme.CheckGreen
import com.duewise.app.ui.theme.CardSurface
import com.duewise.app.ui.theme.TextPrimary
import com.duewise.app.ui.theme.TextSecondary

/** Side padding shared by every screen so content lines up the same way everywhere. */
val ScreenPadding = PaddingValues(horizontal = 20.dp)

/** Outermost container for form and details screens: scrolls, and moves above the keyboard. */
@Composable
fun ScrollScreen(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(ScreenPadding)
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        content = content
    )
}

/** Screen title row with an optional back arrow. */
@Composable
fun ScreenHeader(
    title: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onBack != null) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
            Spacer(Modifier.width(4.dp))
        }
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )
    }
}

/** Small uppercase heading above a group of items, e.g. "UPCOMING TASKS". */
@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        color = TextSecondary,
        modifier = modifier.padding(top = 12.dp)
    )
}

/** Shown in place of a list when the list has nothing in it. */
@Composable
fun EmptyState(message: String, modifier: Modifier = Modifier) {
    DueWiseCard(modifier = modifier.fillMaxWidth()) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            modifier = Modifier.padding(20.dp)
        )
    }
}

/** Label above a value on details screens, e.g. "TARGET DATE" over "September 20, 2026". */
@Composable
fun LabeledValue(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = label.uppercase(), style = MaterialTheme.typography.labelMedium, color = TextSecondary)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
    }
}

/** DueWise logo: a rounded blue square with a clock ring and a green check. */
@Composable
fun AppLogo(modifier: Modifier = Modifier, size: Dp = 48.dp) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(size * 0.25f))
            .background(AccentBlue),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(size * 0.62f)
                .border(width = size * 0.06f, color = CardSurface, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = CheckGreen,
                modifier = Modifier.size(size * 0.4f)
            )
        }
    }
}
