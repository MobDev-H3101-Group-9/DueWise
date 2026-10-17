package com.duewise.app.data

import java.time.LocalDate

/**
 * Starting data, copied from the approved prototype.
 * The app keeps changes in memory while it runs; data persistence is out of scope (MS1, Section V).
 */
object SampleData {

    private fun sep(day: Int): LocalDate = LocalDate.of(2026, 9, day)

    val assignments = listOf(
        Assignment(id = 1, title = "Mobile App Prototype", subject = "Human-Computer Interaction", deadline = sep(30)),
        Assignment(id = 2, title = "Research Paper", subject = "English", deadline = sep(29)),
        Assignment(id = 3, title = "Biology Lab Report", subject = "Biology", deadline = sep(28))
    )

    val tasks = listOf(
        Task(id = 1, assignmentId = 1, name = "Draft user flow", targetDate = sep(20), isCompleted = true),
        Task(id = 2, assignmentId = 1, name = "Create wireframes", targetDate = sep(23)),
        Task(id = 3, assignmentId = 1, name = "Connect prototype screens", targetDate = sep(26)),
        Task(id = 4, assignmentId = 1, name = "Review and revise", targetDate = sep(28)),
        Task(id = 5, assignmentId = 1, name = "Submit prototype", targetDate = sep(30)),
        Task(id = 6, assignmentId = 2, name = "Find sources", targetDate = sep(20)),
        Task(id = 7, assignmentId = 3, name = "Write results", targetDate = sep(25))
    )
}
