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
import com.duewise.app.data.Assignment
import com.duewise.app.data.Task
import com.duewise.app.ui.components.DueWiseCard
import com.duewise.app.ui.components.EmptyState
import com.duewise.app.ui.components.ScreenHeader
import com.duewise.app.ui.components.SectionLabel
import com.duewise.app.ui.theme.AccentBlue
import com.duewise.app.ui.theme.TextPrimary
import com.duewise.app.ui.theme.TextSecondary
import com.duewise.app.util.toDisplayDate

// Reminders: the next 2 tasks, plus the assignment with the nearest final deadline.
@Composable
fun NotificationsScreen(
    upcomingTasks: List<Task>,
    assignmentTitles: Map<Int, String>,
    nearestDeadline: Assignment?,
    onBack: () -> Unit,
    onOpenTask: (Int) -> Unit,
    onOpenAssignment: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { ScreenHeader(title = "Notifications", onBack = onBack) }
        item {
            Text(
                text = "Reminders for your next tasks and deadlines.",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )
        }
        item { SectionLabel("Recent") }

        if (upcomingTasks.isEmpty() && nearestDeadline == null) {
            item { EmptyState("No reminders. You're all caught up.") }
        }

        items(upcomingTasks) { task ->
            ReminderCard(
                label = "TASK REMINDER",
                title = task.name,
                detail = "${assignmentTitles[task.assignmentId]} · Target ${task.targetDate.toDisplayDate()}",
                linkText = "View task →",
                onClick = { onOpenTask(task.id) }
            )
        }

        if (nearestDeadline != null) {
            item {
                ReminderCard(
                    label = "FINAL DEADLINE",
                    title = nearestDeadline.title,
                    detail = "Submit your assignment by ${nearestDeadline.deadline.toDisplayDate()}.",
                    linkText = "View assignment →",
                    onClick = { onOpenAssignment(nearestDeadline.id) }
                )
            }
        }
    }
}

@Composable
private fun ReminderCard(
    label: String,
    title: String,
    detail: String,
    linkText: String,
    onClick: () -> Unit
) {
    DueWiseCard(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = AccentBlue)
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(text = detail, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            Text(
                text = linkText,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = AccentBlue,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}
