package com.duewise.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duewise.app.data.Task
import com.duewise.app.ui.components.AppLogo
import com.duewise.app.ui.components.EmptyState
import com.duewise.app.ui.components.OutlinedActionButton
import com.duewise.app.ui.components.SectionLabel
import com.duewise.app.ui.components.TaskCard
import com.duewise.app.ui.theme.AccentBlue
import com.duewise.app.ui.theme.TextPrimary
import com.duewise.app.ui.theme.TextSecondary
import com.duewise.app.util.toDayHeader
import java.time.LocalDate

/**
 * Entry screen (dashboard). Shows the next three pending tasks, the completed tasks,
 * and the main action: + Add assignment.
 */
@Composable
fun TodayScreen(
    today: LocalDate,
    nextTasks: List<Task>,
    completedTasks: List<Task>,
    assignmentTitles: Map<Int, String>,
    onAddAssignment: () -> Unit,
    onOpenNotifications: () -> Unit,
    onSeeAllUpcoming: () -> Unit,
    onOpenTask: (Int) -> Unit,
    onToggleTask: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "header") {
            TodayHeader(today = today, onOpenNotifications = onOpenNotifications)
        }

        item(key = "add-assignment") {
            OutlinedActionButton(text = "+ Add assignment", onClick = onAddAssignment, textColor = AccentBlue)
        }

        item(key = "upcoming-label") { SectionLabel("Upcoming tasks") }

        if (nextTasks.isEmpty()) {
            item(key = "upcoming-empty") {
                EmptyState("No pending tasks. Tap + Add assignment to plan your next one.")
            }
        } else {
            items(nextTasks, key = { "upcoming-${it.id}" }) { task ->
                TaskCard(
                    task = task,
                    assignmentTitle = assignmentTitles[task.assignmentId],
                    onToggleCompleted = { onToggleTask(task.id) },
                    onOpen = { onOpenTask(task.id) }
                )
            }
            item(key = "see-all") {
                TextButton(onClick = onSeeAllUpcoming) {
                    Text(
                        text = "See all in Upcoming →",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = AccentBlue
                    )
                }
            }
        }

        item(key = "completed-label") { SectionLabel("Completed tasks") }

        if (completedTasks.isEmpty()) {
            item(key = "completed-empty") { EmptyState("Tasks you finish will appear here.") }
        } else {
            items(completedTasks, key = { "completed-${it.id}" }) { task ->
                TaskCard(
                    task = task,
                    assignmentTitle = assignmentTitles[task.assignmentId],
                    onToggleCompleted = { onToggleTask(task.id) },
                    onOpen = { onOpenTask(task.id) }
                )
            }
        }
    }
}

@Composable
private fun TodayHeader(today: LocalDate, onOpenNotifications: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AppLogo(size = 44.dp)
            Spacer(Modifier.width(12.dp))
            Text(
                text = "DueWise",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onOpenNotifications) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications",
                    tint = AccentBlue
                )
            }
        }
        Text(
            text = today.toDayHeader(),
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
