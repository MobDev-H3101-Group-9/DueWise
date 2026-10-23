# DueWise

DueWise is a student assignment planner designed to help college students manage larger assignments by breaking them into smaller tasks with their own target dates.

Instead of only keeping track of the final deadline, students can create an assignment, divide it into smaller tasks, set target dates, and monitor which tasks are still pending or already completed.

## Group Members

- Devie Dela Cruz
- Ibrahim Lagyal
- GV Roma
- Jeah May Pareja

## Course

MO-IT114 Mobile Development Fundamentals  
Bachelor of Science in Information Technology

## Main Features

DueWise currently allows the student to:

- Create an assignment
- Set the assignment's final deadline
- Break an assignment into smaller tasks
- Set target dates for each task
- View pending and upcoming tasks
- View completed tasks
- View assignment and task details
- Edit task information
- Mark tasks as completed or incomplete
- Delete tasks with confirmation
- Delete assignments with confirmation
- View basic task reminders

## How to Run the App

### Requirements

- Android Studio
- Android emulator or physical Android device
- Internet connection during the initial Gradle setup

### Steps

1. Clone or download this repository.
2. Open the project in Android Studio.
3. Wait for Gradle to finish syncing.
4. Select an Android emulator or connect an Android device.
5. Select the `app` run configuration.
6. Click **Run**.
7. The DueWise application will launch on the selected device.

No additional account, database, or external service setup is currently required.

## Technologies Used

- Kotlin
- Jetpack Compose
- Material 3
- Navigation Compose
- Android Studio

## Changes from Milestone 1 Prototype

The Milestone 2 version was developed based on the structure and user flow created during Milestone 1.

The following changes were made:

- Converted the Milestone 1 Figma prototype into a working Android application using Kotlin and Jetpack Compose.
- Added working assignment creation instead of prototype-only sample interactions.
- Added the ability to break assignments into smaller tasks.
- Added target-date selection for individual tasks.
- Added working task completion and undo completion.
- Added task editing and deletion.
- Added assignment deletion with confirmation.
- Connected the Today, Upcoming, Completed, Notifications, Assignment Details, and Task Details screens through working navigation.
- Added visible task state changes when tasks are completed, restored, edited, or deleted.
- Simplified parts of the code to match the programming concepts covered in the course.

These changes were made to turn the Milestone 1 prototype into a functional mobile application while keeping the original focus on individual assignment planning.

## Current Scope

DueWise currently focuses on individual student assignment planning.

The current version does not include:

- AI-generated task plans
- Automatic task scheduling
- Teacher or administrator accounts
- Group collaboration
- Messaging
- Grade tracking
- School portal connections
- External calendar synchronization
- Permanent online account storage

The application currently uses sample and in-memory data for its main assignment and task functions.
