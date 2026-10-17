package com.duewise.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duewise.app.data.TaskDraft
import com.duewise.app.ui.components.DateField
import com.duewise.app.ui.components.DueWiseCard
import com.duewise.app.ui.components.EmptyState
import com.duewise.app.ui.components.LabeledTextField
import com.duewise.app.ui.components.OutlinedActionButton
import com.duewise.app.ui.components.PrimaryButton
import com.duewise.app.ui.components.ScreenHeader
import com.duewise.app.ui.components.ScrollScreen
import com.duewise.app.ui.components.SectionLabel
import com.duewise.app.ui.theme.AccentBlue
import com.duewise.app.ui.theme.DangerRed
import com.duewise.app.ui.theme.TextPrimary
import com.duewise.app.ui.theme.TextSecondary
import com.duewise.app.util.toDisplayDate
import java.time.LocalDate

/**
 * Step 1 of 2: assignment title, subject and final deadline.
 * Nothing is saved here; the values travel to step 2 as navigation arguments.
 */
@Composable
fun NewAssignmentScreen(
    defaultDeadline: LocalDate,
    onBack: () -> Unit,
    onContinue: (title: String, subject: String, deadline: LocalDate) -> Unit
) {
    var title by rememberSaveable { mutableStateOf("") }
    var subject by rememberSaveable { mutableStateOf("") }
    var deadline by rememberSaveable { mutableStateOf(defaultDeadline) }
    val canContinue = title.isNotBlank() && subject.isNotBlank()

    ScrollScreen {
        ScreenHeader(title = "New assignment", onBack = onBack)
        Text("Step 1 of 2 · Assignment details", style = MaterialTheme.typography.bodyLarge, color = TextPrimary)

        LabeledTextField(
            label = "Assignment title",
            value = title,
            onValueChange = { title = it },
            placeholder = "e.g., Research Paper"
        )
        LabeledTextField(
            label = "Subject",
            value = subject,
            onValueChange = { subject = it },
            placeholder = "e.g., Biology"
        )
        DateField(label = "Final deadline", date = deadline, onDateSelected = { deadline = it })

        if (!canContinue) {
            Text(
                text = "Enter a title and a subject to continue.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
        PrimaryButton(
            text = "Continue to Tasks",
            onClick = { onContinue(title.trim(), subject.trim(), deadline) },
            enabled = canContinue,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

/**
 * Step 2 of 2: build the list of smaller tasks, then save the whole plan.
 * The draft list is shown on screen so every + Add task has a visible result.
 */
@Composable
fun AddTasksScreen(
    assignmentTitle: String,
    deadline: LocalDate,
    today: LocalDate,
    onBack: () -> Unit,
    onSavePlan: (List<TaskDraft>) -> Unit
) {
    val drafts = rememberSaveable(saver = TaskDraftListSaver) { mutableStateListOf<TaskDraft>() }
    val defaultDate = if (today.isAfter(deadline)) deadline else today
    var taskName by rememberSaveable { mutableStateOf("") }
    var targetDate by rememberSaveable { mutableStateOf(defaultDate) }
    var showNameError by rememberSaveable { mutableStateOf(false) }

    ScrollScreen {
        ScreenHeader(title = "Add smaller tasks", onBack = onBack)
        Text("Step 2 of 2 · Plan your assignment", style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
        Text(
            text = "$assignmentTitle · Due ${deadline.toDisplayDate()}",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        SectionLabel("Tasks in this plan")
        if (drafts.isEmpty()) {
            EmptyState("No tasks added yet. Add at least one task to save your plan.")
        } else {
            drafts.forEachIndexed { index, draft ->
                DraftTaskRow(draft = draft, onRemove = { drafts.removeAt(index) })
            }
        }

        SectionLabel("Add a task")
        LabeledTextField(
            label = "Task name",
            value = taskName,
            onValueChange = {
                taskName = it
                showNameError = false
            },
            placeholder = "e.g., Prepare presentation slides"
        )
        if (showNameError) {
            Text("Enter a task name first.", style = MaterialTheme.typography.bodyMedium, color = DangerRed)
        }
        DateField(
            label = "Target date",
            date = targetDate,
            onDateSelected = { targetDate = it },
            latestAllowed = deadline
        )
        OutlinedActionButton(
            text = "+ Add task",
            textColor = AccentBlue,
            onClick = {
                if (taskName.isBlank()) {
                    showNameError = true
                } else {
                    drafts.add(TaskDraft(taskName.trim(), targetDate))
                    taskName = ""
                }
            }
        )

        PrimaryButton(
            text = "Save plan & view tasks",
            onClick = { onSavePlan(drafts.toList()) },
            enabled = drafts.isNotEmpty(),
            modifier = Modifier.padding(top = 16.dp)
        )
        OutlinedActionButton(text = "Back to assignment information", onClick = onBack)
    }
}

@Composable
private fun DraftTaskRow(draft: TaskDraft, onRemove: () -> Unit) {
    DueWiseCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(start = 20.dp, end = 4.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(draft.name, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                Text(
                    text = draft.targetDate.toDisplayDate(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = AccentBlue
                )
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Filled.Close, contentDescription = "Remove ${draft.name}", tint = TextSecondary)
            }
        }
    }
}

/** Lets the draft list survive rotation: saved as [name, epochDay, name, epochDay, ...]. */
private val TaskDraftListSaver = listSaver<SnapshotStateList<TaskDraft>, Any>(
    save = { list -> list.flatMap { listOf(it.name, it.targetDate.toEpochDay()) } },
    restore = { flat ->
        flat.chunked(2)
            .map { TaskDraft(name = it[0] as String, targetDate = LocalDate.ofEpochDay(it[1] as Long)) }
            .toMutableStateList()
    }
)
