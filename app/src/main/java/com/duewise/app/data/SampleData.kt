package com.duewise.app.data

import java.time.LocalDate

// Sample data copied from our Figma prototype.
// Changes are kept in memory while the app is open (no database).
object SampleData {

    val assignments = listOf(
        Assignment(1, "Mobile App Prototype", "Human-Computer Interaction", LocalDate.of(2026, 9, 30)),
        Assignment(2, "Research Paper", "English", LocalDate.of(2026, 9, 29)),
        Assignment(3, "Biology Lab Report", "Biology", LocalDate.of(2026, 9, 28))
    )

    val tasks = listOf(
        Task(1, 1, "Draft user flow", LocalDate.of(2026, 9, 20), isCompleted = true),
        Task(2, 1, "Create wireframes", LocalDate.of(2026, 9, 23)),
        Task(3, 1, "Connect prototype screens", LocalDate.of(2026, 9, 26)),
        Task(4, 1, "Review and revise", LocalDate.of(2026, 9, 28)),
        Task(5, 1, "Submit prototype", LocalDate.of(2026, 9, 30)),
        Task(6, 2, "Find sources", LocalDate.of(2026, 9, 20)),
        Task(7, 3, "Write results", LocalDate.of(2026, 9, 25))
    )
}
