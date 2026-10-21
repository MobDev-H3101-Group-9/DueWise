package com.duewise.app.data

import java.time.LocalDate

// An assignment, for example "Biology Lab Report".
data class Assignment(
    val id: Int,
    val title: String,
    val subject: String,
    val deadline: LocalDate
)

// A smaller task inside an assignment, for example "Write results".
data class Task(
    val id: Int,
    val assignmentId: Int,
    val name: String,
    val targetDate: LocalDate,
    val isCompleted: Boolean = false
)

// A task typed on the "Add smaller tasks" screen that is not saved yet.
data class TaskDraft(
    val name: String,
    val targetDate: LocalDate
)
