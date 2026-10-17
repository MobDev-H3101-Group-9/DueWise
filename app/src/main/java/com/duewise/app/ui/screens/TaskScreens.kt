package com.duewise.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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

/** One layout for every task (decision C4): name, parent assignment, dates, then actions. */
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
            Text(
                text = assignment.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = AccentBlue,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clickable(onClickLabel = "Open assignment", onClick = onOpenAssignment)
            )
            Text(text = assignment.subject, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        }

        LabeledValue(label = "Status", value = if (task.isCompleted) "Completed" else "Pending")
        LabeledValue(label = "Target date", value = task.targetDate.toDisplayDate())
        LabeledValue(label = "Assignment deadline", value = assignment.deadline.toDisplayDate())

        TextButton(onClick = onToggleCompleted) {
            Icon(
                imageVector = if (task.isCompleted) Icons.Filled.Refresh else Icons.Filled.Check,
                contentDescription = null,
                tint = TextPrimary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = if (task.isCompleted) "Mark as not done" else "Mark task completed",
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary
            )
        }

        OutlinedActionButton(text = "Edit task", onClick = onEdit)
        OutlinedActionButton(text = "Delete task", onClick = onDelete, textColor = DangerRed)
    }
}

/**
 * Edits a task's name and target date. Typed values are a draft held in this screen:
 * Save changes commits them (state change); Cancel or Back discards them (movement only).
 */
@Composable
fun EditTaskScreen(
    task: Task,
    assignment: Assignment,
    onSave: (name: String, targetDate: LocalDate) -> Unit,
    onCancel: () -> Unit
) {
    var name by rememberSaveable(task.id) { mutableStateOf(task.name) }
    var targetDate by rememberSaveable(task.id) { mutableStateOf(task.targetDate) }

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

        DateField(
            label = "Target date",
            date = targetDate,
            onDateSelected = { targetDate = it },
            latestAllowed = assignment.deadline
        )
        Text(
            text = "Must be on or before the final deadline, ${assignment.deadline.toDisplayDate()}.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        PrimaryButton(
            text = "Save changes",
            onClick = { onSave(name, targetDate) },
            enabled = name.isNotBlank(),
            modifier = Modifier.padding(top = 8.dp)
        )
        OutlinedActionButton(text = "Cancel", onClick = onCancel, centered = true)
    }
}

/** Confirmation before deleting one task. Cancel changes nothing. */
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
