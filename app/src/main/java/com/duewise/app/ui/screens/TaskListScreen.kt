package com.duewise.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.duewise.app.data.Task
import com.duewise.app.ui.components.EmptyState
import com.duewise.app.ui.components.ScreenHeader
import com.duewise.app.ui.components.TaskCard
import com.duewise.app.ui.theme.TextSecondary

/** Upcoming tab: every pending task, earliest target date first. */
@Composable
fun UpcomingScreen(
    tasks: List<Task>,
    assignmentTitles: Map<Int, String>,
    onOpenTask: (Int) -> Unit,
    onToggleTask: (Int) -> Unit
) {
    TaskListScreen(
        title = "Upcoming",
        hint = "All pending tasks, earliest target date first. Tap ○ when a task is done.",
        emptyMessage = "Nothing pending. You're all caught up.",
        tasks = tasks,
        assignmentTitles = assignmentTitles,
        onOpenTask = onOpenTask,
        onToggleTask = onToggleTask
    )
}

/** Completed tab: finished tasks. Tapping ✓ restores a task to pending (undo). */
@Composable
fun CompletedScreen(
    tasks: List<Task>,
    assignmentTitles: Map<Int, String>,
    onOpenTask: (Int) -> Unit,
    onToggleTask: (Int) -> Unit
) {
    TaskListScreen(
        title = "Completed",
        hint = "Tap ✓ to mark a task as not done.",
        emptyMessage = "No completed tasks yet.",
        tasks = tasks,
        assignmentTitles = assignmentTitles,
        onOpenTask = onOpenTask,
        onToggleTask = onToggleTask
    )
}

/** Shared layout for the Upcoming and Completed tabs: title, hint, then a list of task cards. */
@Composable
private fun TaskListScreen(
    title: String,
    hint: String,
    emptyMessage: String,
    tasks: List<Task>,
    assignmentTitles: Map<Int, String>,
    onOpenTask: (Int) -> Unit,
    onToggleTask: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "header") { ScreenHeader(title = title) }
        item(key = "hint") {
            Text(text = hint, style = MaterialTheme.typography.bodyLarge, color = TextSecondary)
        }
        if (tasks.isEmpty()) {
            item(key = "empty") { EmptyState(emptyMessage) }
        } else {
            items(tasks, key = { it.id }) { task ->
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
