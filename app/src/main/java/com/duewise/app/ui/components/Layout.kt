package com.duewise.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.duewise.app.ui.theme.AccentBlue
import com.duewise.app.ui.theme.CardSurface
import com.duewise.app.ui.theme.CheckGreen
import com.duewise.app.ui.theme.TextPrimary
import com.duewise.app.ui.theme.TextSecondary

// Outer container for form and details screens. It scrolls and moves up when the keyboard opens.
@Composable
fun ScrollScreen(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(start = 20.dp, end = 20.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        content = content
    )
}

// Screen title with an optional back arrow.
@Composable
fun ScreenHeader(title: String, onBack: (() -> Unit)? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onBack != null) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
            }
            Spacer(Modifier.width(4.dp))
        }
        Text(text = title, style = MaterialTheme.typography.headlineMedium, color = TextPrimary)
    }
}

// Small uppercase heading, like "UPCOMING TASKS".
@Composable
fun SectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        color = TextSecondary,
        modifier = Modifier.padding(top = 12.dp)
    )
}

// Shown instead of a list when the list is empty.
@Composable
fun EmptyState(message: String) {
    DueWiseCard(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            modifier = Modifier.padding(20.dp)
        )
    }
}

// A label with a value under it, like "TARGET DATE" / "September 20, 2026".
@Composable
fun LabeledValue(label: String, value: String) {
    Column {
        Text(text = label.uppercase(), style = MaterialTheme.typography.labelMedium, color = TextSecondary)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
    }
}

// DueWise logo: blue rounded square with a white ring and a green check.
@Composable
fun AppLogo(size: Dp) {
    Box(
        modifier = Modifier
            .size(size)
            .background(AccentBlue, RoundedCornerShape(size / 4)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(size * 0.62f)
                .border(3.dp, CardSurface, CircleShape),
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
