package com.duewise.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duewise.app.data.Reminder
import com.duewise.app.data.ReminderKind
import com.duewise.app.ui.components.DueWiseCard
import com.duewise.app.ui.components.EmptyState
import com.duewise.app.ui.components.ScreenHeader
import com.duewise.app.ui.components.SectionLabel
import com.duewise.app.ui.theme.AccentBlue
import com.duewise.app.ui.theme.TextPrimary
import com.duewise.app.ui.theme.TextSecondary

/** Reminder cards for the next tasks and the nearest final deadline. Each card opens Task details. */
@Composable
fun NotificationsScreen(
    reminders: List<Reminder>,
    onBack: () -> Unit,
    onOpenTask: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "header") { ScreenHeader(title = "Notifications", onBack = onBack) }
        item(key = "hint") {
            Text(
                text = "Reminders for your next tasks and deadlines.",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )
        }
        item(key = "label") { SectionLabel("Recent") }
        if (reminders.isEmpty()) {
            item(key = "empty") { EmptyState("No reminders. You're all caught up.") }
        } else {
            items(reminders, key = { "${it.kind}-${it.taskId}-${it.title}" }) { reminder ->
                ReminderCard(reminder = reminder, onOpen = { onOpenTask(reminder.taskId) })
            }
        }
    }
}

@Composable
private fun ReminderCard(reminder: Reminder, onOpen: () -> Unit) {
    DueWiseCard(onClick = onOpen, modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = reminder.kind.label, style = MaterialTheme.typography.labelMedium, color = AccentBlue)
            Text(
                text = reminder.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(text = reminder.detail, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            Text(
                text = if (reminder.kind == ReminderKind.FinalDeadline) "View next task →" else "View task →",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = AccentBlue,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}
