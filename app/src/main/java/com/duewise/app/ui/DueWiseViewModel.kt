package com.duewise.app.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.duewise.app.data.Assignment
import com.duewise.app.data.Reminder
import com.duewise.app.data.ReminderKind
import com.duewise.app.data.SampleData
import com.duewise.app.data.Task
import com.duewise.app.data.TaskDraft
import com.duewise.app.util.toDisplayDate
import java.time.LocalDate

/**
 * Holds the app's shared state: every assignment and task.
 *
 * Both lists are Compose state lists, so any screen that reads them recomposes automatically
 * when an event below changes them. Screens never change the lists directly; they call these
 * functions through the navigation layer.
 *
 * The ViewModel survives screen rotation, so state does not reset unexpectedly.
 */
class DueWiseViewModel : ViewModel() {

    private val assignments = mutableStateListOf<Assignment>().apply { addAll(SampleData.assignments) }
    private val tasks = mutableStateListOf<Task>().apply { addAll(SampleData.tasks) }

    private var nextAssignmentId = (SampleData.assignments.maxOfOrNull { it.id } ?: 0) + 1
    private var nextTaskId = (SampleData.tasks.maxOfOrNull { it.id } ?: 0) + 1

    // ---------- Reading state ----------

    fun assignment(id: Int): Assignment? = assignments.firstOrNull { it.id == id }

    fun task(id: Int): Task? = tasks.firstOrNull { it.id == id }

    /** Assignment titles by id, so task cards can show their parent assignment. */
    fun assignmentTitles(): Map<Int, String> = assignments.associate { it.id to it.title }

    /** All unfinished tasks, earliest target date first (decision A4). */
    fun pendingTasks(): List<Task> =
        tasks.filter { !it.isCompleted }.sortedWith(compareBy({ it.targetDate }, { it.name }))

    /** Finished tasks, earliest target date first. */
    fun completedTasks(): List<Task> =
        tasks.filter { it.isCompleted }.sortedWith(compareBy({ it.targetDate }, { it.name }))

    /** Today shows only the next three pending tasks (decision A3). */
    fun nextPendingTasks(count: Int = TODAY_TASK_LIMIT): List<Task> = pendingTasks().take(count)

    fun tasksFor(assignmentId: Int): List<Task> =
        tasks.filter { it.assignmentId == assignmentId }.sortedWith(compareBy({ it.targetDate }, { it.name }))

    /**
     * Notification cards built from the current tasks:
     * a reminder for each of the next two pending tasks, then a final-deadline card for the
     * assignment with the nearest deadline that still has unfinished tasks.
     */
    fun reminders(): List<Reminder> {
        val titles = assignmentTitles()
        val pending = pendingTasks()

        val taskReminders = pending.take(2).map { task ->
            Reminder(
                kind = ReminderKind.TaskReminder,
                title = task.name,
                detail = "${titles[task.assignmentId].orEmpty()} · Target ${task.targetDate.toDisplayDate()}",
                taskId = task.id
            )
        }

        val deadlineReminder = assignments
            .filter { assignment -> pending.any { it.assignmentId == assignment.id } }
            .minByOrNull { it.deadline }
            ?.let { assignment ->
                Reminder(
                    kind = ReminderKind.FinalDeadline,
                    title = assignment.title,
                    detail = "Submit your assignment by ${assignment.deadline.toDisplayDate()}.",
                    taskId = pending.first { it.assignmentId == assignment.id }.id
                )
            }

        return taskReminders + listOfNotNull(deadlineReminder)
    }

    // ---------- Events that change state ----------

    /** Marks a task done, or restores it to pending if it was already done. */
    fun toggleCompleted(taskId: Int) {
        val index = tasks.indexOfFirst { it.id == taskId }
        if (index >= 0) {
            tasks[index] = tasks[index].copy(isCompleted = !tasks[index].isCompleted)
        }
    }

    fun updateTask(taskId: Int, name: String, targetDate: LocalDate) {
        val index = tasks.indexOfFirst { it.id == taskId }
        if (index >= 0) {
            tasks[index] = tasks[index].copy(name = name.trim(), targetDate = targetDate)
        }
    }

    /** Removes only the selected task; other tasks in its assignment remain. */
    fun deleteTask(taskId: Int) {
        tasks.removeAll { it.id == taskId }
    }

    /** Removes the assignment and all of its tasks. */
    fun deleteAssignment(assignmentId: Int) {
        tasks.removeAll { it.assignmentId == assignmentId }
        assignments.removeAll { it.id == assignmentId }
    }

    /** Saves a new assignment with its planned tasks. */
    fun createAssignment(title: String, subject: String, deadline: LocalDate, drafts: List<TaskDraft>) {
        val assignmentId = nextAssignmentId++
        assignments.add(Assignment(assignmentId, title.trim(), subject.trim(), deadline))
        drafts.forEach { draft ->
            tasks.add(Task(nextTaskId++, assignmentId, draft.name.trim(), draft.targetDate))
        }
    }

    companion object {
        const val TODAY_TASK_LIMIT = 3
    }
}
