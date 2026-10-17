package com.duewise.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duewise.app.data.Assignment
import com.duewise.app.data.Task
import com.duewise.app.ui.components.DueWiseCard
import com.duewise.app.ui.components.EmptyState
import com.duewise.app.ui.components.OutlinedActionButton
import com.duewise.app.ui.components.ScreenHeader
import com.duewise.app.ui.components.ScrollScreen
import com.duewise.app.ui.components.SectionLabel
import com.duewise.app.ui.components.TaskCard
import com.duewise.app.ui.theme.CardBorder
import com.duewise.app.ui.theme.DangerRed
import com.duewise.app.ui.theme.TextPrimary
import com.duewise.app.ui.theme.TextSecondary
import com.duewise.app.util.toDisplayDate

/** An assignment with all of its tasks, sorted by target date. */
@Composable
fun AssignmentDetailsScreen(
    assignment: Assignment,
    tasks: List<Task>,
    onBack: () -> Unit,
    onOpenTask: (Int) -> Unit,
    onToggleTask: (Int) -> Unit,
    onDeleteAssignment: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "header") { ScreenHeader(title = "Assignment details", onBack = onBack) }
        item(key = "summary") { AssignmentSummaryCard(assignment) }
        item(key = "tasks-label") { SectionLabel("Tasks") }
        if (tasks.isEmpty()) {
            item(key = "empty") { EmptyState("This assignment has no tasks.") }
        } else {
            items(tasks, key = { it.id }) { task ->
                TaskCard(
                    task = task,
                    assignmentTitle = null,
                    onToggleCompleted = { onToggleTask(task.id) },
                    onOpen = { onOpenTask(task.id) }
                )
            }
        }
        item(key = "delete") {
            OutlinedActionButton(
                text = "Delete assignment",
                onClick = onDeleteAssignment,
                textColor = DangerRed,
                centered = true,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}

@Composable
private fun AssignmentSummaryCard(assignment: Assignment) {
    DueWiseCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = assignment.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(text = assignment.subject, style = MaterialTheme.typography.bodyLarge, color = TextSecondary)
            HorizontalDivider(color = CardBorder, modifier = Modifier.padding(vertical = 8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.DateRange,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = "Final deadline: ${assignment.deadline.toDisplayDate()}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary
                )
            }
        }
    }
}

/** Confirmation before deleting an assignment and all of its tasks. Cancel changes nothing. */
@Composable
fun DeleteAssignmentScreen(
    assignment: Assignment,
    taskCount: Int,
    onCancel: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    ScrollScreen {
        ScreenHeader(title = "Delete assignment", onBack = onCancel)
        Text(text = "Delete this assignment?", style = MaterialTheme.typography.titleLarge, color = TextPrimary)
        Text(text = assignment.title, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
        Text(
            text = "The assignment and its $taskCount ${if (taskCount == 1) "task" else "tasks"} will be removed from your lists.",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )
        OutlinedActionButton(text = "Cancel — keep assignment", onClick = onCancel)
        OutlinedActionButton(text = "Delete assignment", onClick = onConfirmDelete, textColor = DangerRed)
    }
}
