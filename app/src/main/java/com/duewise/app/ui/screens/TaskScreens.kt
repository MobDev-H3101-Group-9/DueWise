package com.duewise.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.duewise.app.data.Assignment
import com.duewise.app.data.Task
import com.duewise.app.ui.components.DateField
import com.duewise.app.ui.components.LabeledTextField
import com.duewise.app.ui.components.LabeledValue
import com.duewise.app.ui.components.OutlinedActionButton
import com.duewise.app.ui.components.PrimaryButton
import com.duewise.app.ui.components.ScreenHeader
import com.duewise.app.ui.components.ScrollScreen
import com.duewise.app.ui.theme.AccentBlue
import com.duewise.app.ui.theme.DangerRed
import com.duewise.app.ui.theme.TextPrimary
import com.duewise.app.ui.theme.TextSecondary
import com.duewise.app.util.toDisplayDate
import java.time.LocalDate

// Details of one task, with buttons to complete, edit, or delete it.
@Composable
fun TaskDetailsScreen(
    task: Task,
    assignment: Assignment,
    onBack: () -> Unit,
    onOpenAssignment: () -> Unit,
    onToggleCompleted: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    ScrollScreen {
        ScreenHeader(title = "Task details", onBack = onBack)

        Column {
            Text(text = task.name, style = MaterialTheme.typography.titleLarge, color = TextPrimary)
            // Tapping the assignment name opens Assignment details
            Text(
                text = assignment.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = AccentBlue,
                modifier = Modifier.clickable { onOpenAssignment() }
            )
            Text(text = assignment.subject, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        }

        LabeledValue(label = "Status", value = if (task.isCompleted) "Completed" else "Pending")
        LabeledValue(label = "Target date", value = task.targetDate.toDisplayDate())
        LabeledValue(label = "Assignment deadline", value = assignment.deadline.toDisplayDate())

        TextButton(onClick = onToggleCompleted) {
            Text(
                text = if (task.isCompleted) "↺ Mark as not done" else "✓ Mark task completed",
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary
            )
        }

        OutlinedActionButton(text = "Edit task", onClick = onEdit)
        OutlinedActionButton(text = "Delete task", onClick = onDelete, textColor = DangerRed)
    }
}

// Edit a task's name and target date.
// The typed values are only saved when the user taps "Save changes". Cancel or Back throws them away.
@Composable
fun EditTaskScreen(
    task: Task,
    assignment: Assignment,
    onSave: (String, LocalDate) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf(task.name) }
    var targetDate by remember { mutableStateOf(task.targetDate) }

    val dateIsTooLate = targetDate.isAfter(assignment.deadline)
    val canSave = name.isNotBlank() && !dateIsTooLate

    ScrollScreen {
        ScreenHeader(title = "Edit task", onBack = onCancel)
        Text(
            text = "${assignment.title} · Update task and target date",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )

        LabeledTextField(
            label = "Task name",
            value = name,
            onValueChange = { name = it },
            placeholder = "e.g., Find sources"
        )
        if (name.isBlank()) {
            Text("Enter a task name to save.", style = MaterialTheme.typography.bodyMedium, color = DangerRed)
        }

        DateField(label = "Target date", date = targetDate, onDateSelected = { targetDate = it })
        if (dateIsTooLate) {
            Text(
                text = "Pick a date on or before the final deadline, ${assignment.deadline.toDisplayDate()}.",
                style = MaterialTheme.typography.bodyMedium,
                color = DangerRed
            )
        }

        PrimaryButton(text = "Save changes", onClick = { onSave(name, targetDate) }, enabled = canSave)
        OutlinedActionButton(text = "Cancel", onClick = onCancel, centered = true)
    }
}

// Asks before deleting one task. Cancel changes nothing.
@Composable
fun DeleteTaskScreen(
    task: Task,
    onCancel: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    ScrollScreen {
        ScreenHeader(title = "Delete task", onBack = onCancel)
        Text(text = task.name, style = MaterialTheme.typography.titleLarge, color = TextPrimary)
        Text(
            text = "Delete this task? The other tasks in its assignment will remain.",
            style = MaterialTheme.typography.bodyLarge,
            color = TextPrimary
        )
        OutlinedActionButton(text = "Cancel — keep task", onClick = onCancel)
        OutlinedActionButton(text = "Delete task", onClick = onConfirmDelete, textColor = DangerRed)
    }
}
