package com.duewise.app.navigation

import android.net.Uri
import java.time.LocalDate

/**
 * Every route in the app, defined in one place.
 * Screens never build route strings themselves; they call the functions below.
 * Only primitive values (Int, String, Long) are passed as arguments.
 */
object Routes {

    // Argument names
    const val ARG_TASK_ID = "taskId"
    const val ARG_ASSIGNMENT_ID = "assignmentId"
    const val ARG_TITLE = "title"
    const val ARG_SUBJECT = "subject"
    const val ARG_DEADLINE = "deadline" // LocalDate stored as an epoch day (Long)

    // Routes without arguments
    const val LOGIN = "login"
    const val TODAY = "today"
    const val UPCOMING = "upcoming"
    const val COMPLETED = "completed"
    const val SETTINGS = "settings"
    const val NOTIFICATIONS = "notifications"
    const val NEW_ASSIGNMENT = "new_assignment"

    // Routes with arguments: the pattern registered in the NavHost, plus a builder for navigation calls
    private const val TASK_DETAILS_BASE = "task_details"
    const val TASK_DETAILS = "$TASK_DETAILS_BASE/{$ARG_TASK_ID}"
    fun taskDetails(taskId: Int) = "$TASK_DETAILS_BASE/$taskId"

    private const val EDIT_TASK_BASE = "edit_task"
    const val EDIT_TASK = "$EDIT_TASK_BASE/{$ARG_TASK_ID}"
    fun editTask(taskId: Int) = "$EDIT_TASK_BASE/$taskId"

    private const val DELETE_TASK_BASE = "delete_task"
    const val DELETE_TASK = "$DELETE_TASK_BASE/{$ARG_TASK_ID}"
    fun deleteTask(taskId: Int) = "$DELETE_TASK_BASE/$taskId"

    private const val ASSIGNMENT_DETAILS_BASE = "assignment_details"
    const val ASSIGNMENT_DETAILS = "$ASSIGNMENT_DETAILS_BASE/{$ARG_ASSIGNMENT_ID}"
    fun assignmentDetails(assignmentId: Int) = "$ASSIGNMENT_DETAILS_BASE/$assignmentId"

    private const val DELETE_ASSIGNMENT_BASE = "delete_assignment"
    const val DELETE_ASSIGNMENT = "$DELETE_ASSIGNMENT_BASE/{$ARG_ASSIGNMENT_ID}"
    fun deleteAssignment(assignmentId: Int) = "$DELETE_ASSIGNMENT_BASE/$assignmentId"

    private const val ADD_TASKS_BASE = "add_tasks"
    const val ADD_TASKS = "$ADD_TASKS_BASE/{$ARG_TITLE}/{$ARG_SUBJECT}/{$ARG_DEADLINE}"
    fun addTasks(title: String, subject: String, deadline: LocalDate) =
        "$ADD_TASKS_BASE/${Uri.encode(title)}/${Uri.encode(subject)}/${deadline.toEpochDay()}"
}
