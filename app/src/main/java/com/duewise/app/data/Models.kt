package com.duewise.app.data

import java.time.LocalDate

/** The overall academic deliverable, for example "Biology Lab Report". */
data class Assignment(
    val id: Int,
    val title: String,
    val subject: String,
    val deadline: LocalDate
)

/** A smaller action inside an assignment, for example "Write results". */
data class Task(
    val id: Int,
    val assignmentId: Int,
    val name: String,
    val targetDate: LocalDate,
    val isCompleted: Boolean = false
)

/** A task typed on the Add smaller tasks screen that is not saved yet. */
data class TaskDraft(
    val name: String,
    val targetDate: LocalDate
)

/** One card on the Notifications screen, derived from the current tasks. */
data class Reminder(
    val kind: ReminderKind,
    val title: String,
    val detail: String,
    val taskId: Int
)

enum class ReminderKind(val label: String) {
    TaskReminder("TASK REMINDER"),
    FinalDeadline("FINAL DEADLINE")
}
