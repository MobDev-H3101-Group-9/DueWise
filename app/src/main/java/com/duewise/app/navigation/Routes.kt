package com.duewise.app.navigation

import android.net.Uri
import java.time.LocalDate

// All route names are kept here, in one place.
// Screens with arguments have a route pattern (used in the NavHost)
// and a function that builds the real route (used when navigating).
object Routes {

    // Argument names
    const val ARG_TASK_ID = "taskId"
    const val ARG_ASSIGNMENT_ID = "assignmentId"
    const val ARG_TITLE = "title"
    const val ARG_SUBJECT = "subject"
    const val ARG_DEADLINE = "deadline"

    // Screens without arguments
    const val LOGIN = "login"
    const val TODAY = "today"
    const val UPCOMING = "upcoming"
    const val COMPLETED = "completed"
    const val SETTINGS = "settings"
    const val NOTIFICATIONS = "notifications"
    const val NEW_ASSIGNMENT = "new_assignment"

    // Screens with arguments
    const val TASK_DETAILS = "task_details/{$ARG_TASK_ID}"
    fun taskDetails(taskId: Int) = "task_details/$taskId"

    const val EDIT_TASK = "edit_task/{$ARG_TASK_ID}"
    fun editTask(taskId: Int) = "edit_task/$taskId"

    const val DELETE_TASK = "delete_task/{$ARG_TASK_ID}"
    fun deleteTask(taskId: Int) = "delete_task/$taskId"

    const val ASSIGNMENT_DETAILS = "assignment_details/{$ARG_ASSIGNMENT_ID}"
    fun assignmentDetails(assignmentId: Int) = "assignment_details/$assignmentId"

    const val DELETE_ASSIGNMENT = "delete_assignment/{$ARG_ASSIGNMENT_ID}"
    fun deleteAssignment(assignmentId: Int) = "delete_assignment/$assignmentId"

    // The deadline is sent as a number (days since 1970), because routes can only carry simple values.
    // Uri.encode makes titles with "/" or spaces safe to put in a route.
    const val ADD_TASKS = "add_tasks/{$ARG_TITLE}/{$ARG_SUBJECT}/{$ARG_DEADLINE}"
    fun addTasks(title: String, subject: String, deadline: LocalDate) =
        "add_tasks/${Uri.encode(title)}/${Uri.encode(subject)}/${deadline.toEpochDay()}"
}
