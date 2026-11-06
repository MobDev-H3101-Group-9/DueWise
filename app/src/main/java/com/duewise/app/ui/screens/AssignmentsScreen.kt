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
import com.duewise.app.ui.components.OutlinedActionButton
import com.duewise.app.ui.components.ScreenHeader
import com.duewise.app.ui.theme.AccentBlue
import com.duewise.app.ui.theme.TextPrimary
import com.duewise.app.ui.theme.TextSecondary
import com.duewise.app.util.toDisplayDate

// Assignments tab: every assignment, even ones that have no tasks left.
@Composable
fun AssignmentsScreen(
    assignments: List<Assignment>,
    allTasks: List<Task>,
    onAddAssignment: () -> Unit,
    onOpenAssignment: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { ScreenHeader(title = "Assignments") }
        item {
            Text(
                text = "All your assignments, nearest deadline first.",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )
        }
        item {
            OutlinedActionButton(text = "+ Add assignment", onClick = onAddAssignment, textColor = AccentBlue)
        }

        if (assignments.isEmpty()) {
            item { EmptyState("No assignments yet. Tap + Add assignment to plan one.") }
        } else {
            items(assignments) { assignment ->
                // Count this assignment's tasks and how many are done
                val tasks = allTasks.filter { it.assignmentId == assignment.id }
                val doneCount = tasks.count { it.isCompleted }

                AssignmentCard(
                    assignment = assignment,
                    progress = if (tasks.isEmpty()) "No tasks yet" else "$doneCount of ${tasks.size} tasks done",
                    onClick = { onOpenAssignment(assignment.id) }
                )
            }
        }
    }
}

@Composable
private fun AssignmentCard(assignment: Assignment, progress: String, onClick: () -> Unit) {
    DueWiseCard(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = assignment.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(text = assignment.subject, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            Text(
                text = "Due ${assignment.deadline.toDisplayDate()}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = AccentBlue
            )
            Text(text = progress, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        }
    }
}