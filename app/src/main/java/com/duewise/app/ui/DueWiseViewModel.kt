package com.duewise.app.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.duewise.app.data.Assignment
import com.duewise.app.data.SampleData
import com.duewise.app.data.Task
import com.duewise.app.data.TaskDraft
import java.time.LocalDate

// Holds the app's data: all assignments and tasks.
// Both lists are Compose state, so every screen that shows them updates automatically.
class DueWiseViewModel : ViewModel() {

    private val assignments = mutableStateListOf<Assignment>()
    private val tasks = mutableStateListOf<Task>()

    // Used to give new assignments and tasks a unique id.
    private var nextAssignmentId = 4
    private var nextTaskId = 8

    init {
        assignments.addAll(SampleData.assignments)
        tasks.addAll(SampleData.tasks)
    }

    // ---------- Reading data ----------

    fun assignment(id: Int): Assignment? = assignments.find { it.id == id }

    fun task(id: Int): Task? = tasks.find { it.id == id }

    // Assignment title for each assignment id, so task cards can show it.
    fun assignmentTitles(): Map<Int, String> = assignments.associate { it.id to it.title }

    // Unfinished tasks, earliest target date first.
    fun pendingTasks(): List<Task> = tasks.filter { !it.isCompleted }.sortedBy { it.targetDate }

    // Finished tasks, earliest target date first.
    fun completedTasks(): List<Task> = tasks.filter { it.isCompleted }.sortedBy { it.targetDate }

    // Today screen shows only the next 3 pending tasks.
    fun nextPendingTasks(): List<Task> = pendingTasks().take(3)

    fun tasksFor(assignmentId: Int): List<Task> =
        tasks.filter { it.assignmentId == assignmentId }.sortedBy { it.targetDate }

    // The assignment due soonest, shown as the "Final deadline" notification.
    fun nearestDeadline(): Assignment? = assignments.minByOrNull { it.deadline }

    // ---------- Changing data ----------

    // Marks a task as done, or back to not done if it was already done.
    fun toggleCompleted(taskId: Int) {
        val index = tasks.indexOfFirst { it.id == taskId }
        if (index != -1) {
            val task = tasks[index]
            tasks[index] = task.copy(isCompleted = !task.isCompleted)
        }
    }

    fun updateTask(taskId: Int, name: String, targetDate: LocalDate) {
        val index = tasks.indexOfFirst { it.id == taskId }
        if (index != -1) {
            tasks[index] = tasks[index].copy(name = name.trim(), targetDate = targetDate)
        }
    }

    // Deletes one task. The other tasks in the assignment stay.
    fun deleteTask(taskId: Int) {
        tasks.removeAll { it.id == taskId }
    }

    // Deletes the assignment and all of its tasks.
    fun deleteAssignment(assignmentId: Int) {
        tasks.removeAll { it.assignmentId == assignmentId }
        assignments.removeAll { it.id == assignmentId }
    }

    // Saves a new assignment together with its tasks.
    fun createAssignment(title: String, subject: String, deadline: LocalDate, drafts: List<TaskDraft>) {
        val assignmentId = nextAssignmentId
        nextAssignmentId++
        assignments.add(Assignment(assignmentId, title.trim(), subject.trim(), deadline))

        for (draft in drafts) {
            tasks.add(Task(nextTaskId, assignmentId, draft.name.trim(), draft.targetDate))
            nextTaskId++
        }
    }
}
