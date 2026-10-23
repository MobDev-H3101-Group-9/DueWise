# DueWise

Student Assignment Planner  
MO-IT114 Mobile Development Fundamentals

## About the Project

DueWise is an Android student assignment planner designed to help students manage larger school assignments by breaking them into smaller tasks with their own target dates.

Instead of only recording the final deadline, students can create an assignment, divide it into smaller tasks, set target dates, and track which tasks have already been completed.

The project is built using Kotlin and Jetpack Compose.

## Main Features

DueWise currently allows the student to:

- View pending tasks from the Today screen
- View upcoming tasks ordered by target date
- View completed tasks
- Create a new assignment
- Set an assignment deadline
- Break an assignment into smaller tasks
- Set a target date for each task
- View task details
- Edit a task name or target date
- Mark a task as completed or incomplete
- Delete a task with confirmation
- View assignment details
- Delete an assignment with confirmation
- View basic task reminders through the Notifications screen

The app uses one student user and focuses on individual assignment planning.

## Main User Flow

The primary task flow is:

`Today → Add Assignment → Enter Assignment Details → Add Tasks → Set Target Dates → Save Plan → View Upcoming Tasks`

After creating the plan, the student can open a task to edit it, mark it as completed, or delete it.

## Project Structure

The main source files are located under:

`app/src/main/java/com/duewise/app/`

### Data

`Models.kt`

Contains the main data classes:

- Assignment
- Task
- TaskDraft

`SampleData.kt`

Contains sample assignments and tasks used when the app starts.

### Navigation

`Routes.kt`

Contains the navigation routes used by the application.

`DueWiseNavHost.kt`

Connects the screens and controls navigation between them.

`BottomNavBar.kt`

Contains the main navigation tabs:

- Today
- Upcoming
- Completed
- Settings

### UI

`DueWiseViewModel.kt`

Stores the current assignments and tasks and contains the functions used to create, edit, complete, and delete them.

The `screens` folder contains the different screens of the app, including:

- Today
- Upcoming
- Completed
- Task Details
- Assignment Details
- New Assignment
- Add Tasks
- Notifications
- Settings
- Login

The `components` folder contains reusable UI elements such as buttons, task cards, text fields, dialogs, and layouts.

## Task and Assignment Dates

DueWise uses two different types of dates:

- Final Deadline: The date when the entire assignment is due.
- Target Date: The planned date for completing one smaller task.

A task's target date should not be later than the assignment's final deadline.

## Task Status

A task can be either:

- Pending
- Completed

When a student marks a task as completed, its status is updated and it appears in the Completed section.

The student can also restore a completed task if it was marked as finished by mistake.

## Current Scope

The current version focuses on the main assignment-planning workflow.

The following features are not included in the current project:

- Automatic task generation
- AI-generated schedules
- Teacher or administrator accounts
- Group collaboration
- Messaging
- Grade tracking
- Study timers
- School portal connections
- External calendar synchronization
- Online payments

Authentication is currently represented only through the user interface. The project does not yet use a real authentication service.

The current application also stores its working data in memory and does not yet use permanent database storage.

## Technology Used

- Kotlin
- Jetpack Compose
- Android Studio
- Material 3
- Navigation Compose

The project currently targets Android smartphones in portrait orientation.

## Running the Project

1. Open the project in Android Studio.
2. Wait for Gradle sync to finish.
3. Select an Android emulator or connected Android device.
4. Run the application.
5. The app will open on the Today screen.

## Group Members

- Devie Dela Cruz
- Ibrahim Lagyal
- GV Roma
- Jeah May Pareja

## Course

MO-IT114 Mobile Development Fundamentals  
Bachelor of Science in Information Technology
