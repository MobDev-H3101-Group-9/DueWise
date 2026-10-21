package com.duewise.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.duewise.app.ui.DueWiseViewModel
import com.duewise.app.ui.screens.AddTasksScreen
import com.duewise.app.ui.screens.AssignmentDetailsScreen
import com.duewise.app.ui.screens.CompletedScreen
import com.duewise.app.ui.screens.DeleteAssignmentScreen
import com.duewise.app.ui.screens.DeleteTaskScreen
import com.duewise.app.ui.screens.EditTaskScreen
import com.duewise.app.ui.screens.LoginScreen
import com.duewise.app.ui.screens.NewAssignmentScreen
import com.duewise.app.ui.screens.NotificationsScreen
import com.duewise.app.ui.screens.SettingsScreen
import com.duewise.app.ui.screens.TaskDetailsScreen
import com.duewise.app.ui.screens.TodayScreen
import com.duewise.app.ui.screens.UpcomingScreen
import com.duewise.app.ui.theme.AppBackground
import java.time.LocalDate

// The app's main layout: one NavController, the screens, and the bottom bar.
// All navigation code is in this file. Screens only tell us what the user tapped.
@Composable
fun DueWiseApp(viewModel: DueWiseViewModel) {
    val navController = rememberNavController()

    // Show the bottom bar only on the 4 tab screens.
    val currentEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentEntry?.destination?.route
    val showBottomBar = BottomTab.entries.any { it.route == currentRoute }

    Scaffold(
        containerColor = AppBackground,
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    currentRoute = currentRoute,
                    onTabSelected = { route -> goToTab(navController, route) }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.TODAY,
            modifier = Modifier.padding(innerPadding)
        ) {

            // ---------- Bottom bar tabs ----------

            composable(Routes.TODAY) { entry ->
                TodayScreen(
                    nextTasks = viewModel.nextPendingTasks(),
                    completedTasks = viewModel.completedTasks(),
                    assignmentTitles = viewModel.assignmentTitles(),
                    onAddAssignment = {
                        if (entry.isResumed()) navController.navigate(Routes.NEW_ASSIGNMENT)
                    },
                    onOpenNotifications = {
                        if (entry.isResumed()) navController.navigate(Routes.NOTIFICATIONS)
                    },
                    onSeeAllUpcoming = {
                        if (entry.isResumed()) goToTab(navController, Routes.UPCOMING)
                    },
                    onOpenTask = { taskId ->
                        if (entry.isResumed()) navController.navigate(Routes.taskDetails(taskId))
                    },
                    onToggleTask = { taskId -> viewModel.toggleCompleted(taskId) }
                )
            }

            composable(Routes.UPCOMING) { entry ->
                UpcomingScreen(
                    tasks = viewModel.pendingTasks(),
                    assignmentTitles = viewModel.assignmentTitles(),
                    onOpenTask = { taskId ->
                        if (entry.isResumed()) navController.navigate(Routes.taskDetails(taskId))
                    },
                    onToggleTask = { taskId -> viewModel.toggleCompleted(taskId) }
                )
            }

            composable(Routes.COMPLETED) { entry ->
                CompletedScreen(
                    tasks = viewModel.completedTasks(),
                    assignmentTitles = viewModel.assignmentTitles(),
                    onOpenTask = { taskId ->
                        if (entry.isResumed()) navController.navigate(Routes.taskDetails(taskId))
                    },
                    onToggleTask = { taskId -> viewModel.toggleCompleted(taskId) }
                )
            }

            composable(Routes.SETTINGS) { entry ->
                SettingsScreen(
                    onLogOut = {
                        if (entry.isResumed()) {
                            // Clear every screen so Back cannot return into the app after logging out.
                            navController.navigate(Routes.LOGIN) {
                                popUpTo(navController.graph.id) { inclusive = true }
                            }
                        }
                    }
                )
            }

            // ---------- Login (only reached after Log Out) ----------

            composable(Routes.LOGIN) { entry ->
                LoginScreen(
                    onSignIn = {
                        if (entry.isResumed()) {
                            navController.navigate(Routes.TODAY) {
                                popUpTo(Routes.LOGIN) { inclusive = true }
                            }
                        }
                    }
                )
            }

            // ---------- Notifications ----------

            composable(Routes.NOTIFICATIONS) { entry ->
                NotificationsScreen(
                    upcomingTasks = viewModel.pendingTasks().take(2),
                    assignmentTitles = viewModel.assignmentTitles(),
                    nearestDeadline = viewModel.nearestDeadline(),
                    onBack = {
                        if (entry.isResumed()) navController.popBackStack()
                    },
                    onOpenTask = { taskId ->
                        if (entry.isResumed()) navController.navigate(Routes.taskDetails(taskId))
                    },
                    onOpenAssignment = { assignmentId ->
                        if (entry.isResumed()) navController.navigate(Routes.assignmentDetails(assignmentId))
                    }
                )
            }

            // ---------- Task screens (argument: taskId) ----------

            composable(
                route = Routes.TASK_DETAILS,
                arguments = listOf(navArgument(Routes.ARG_TASK_ID) { type = NavType.IntType })
            ) { entry ->
                val taskId = entry.arguments?.getInt(Routes.ARG_TASK_ID) ?: 0
                val task = viewModel.task(taskId)
                val assignment = if (task != null) viewModel.assignment(task.assignmentId) else null

                // task can be null for a moment right after it is deleted, so check first.
                if (task != null && assignment != null) {
                    TaskDetailsScreen(
                        task = task,
                        assignment = assignment,
                        onBack = {
                            if (entry.isResumed()) navController.popBackStack()
                        },
                        onOpenAssignment = {
                            if (entry.isResumed()) {
                                navController.navigate(Routes.assignmentDetails(assignment.id)) {
                                    launchSingleTop = true
                                }
                            }
                        },
                        onToggleCompleted = { viewModel.toggleCompleted(task.id) },
                        onEdit = {
                            if (entry.isResumed()) navController.navigate(Routes.editTask(task.id))
                        },
                        onDelete = {
                            if (entry.isResumed()) navController.navigate(Routes.deleteTask(task.id))
                        }
                    )
                }
            }

            composable(
                route = Routes.EDIT_TASK,
                arguments = listOf(navArgument(Routes.ARG_TASK_ID) { type = NavType.IntType })
            ) { entry ->
                val taskId = entry.arguments?.getInt(Routes.ARG_TASK_ID) ?: 0
                val task = viewModel.task(taskId)
                val assignment = if (task != null) viewModel.assignment(task.assignmentId) else null

                if (task != null && assignment != null) {
                    EditTaskScreen(
                        task = task,
                        assignment = assignment,
                        onSave = { name, targetDate ->
                            if (entry.isResumed()) {
                                viewModel.updateTask(task.id, name, targetDate)
                                navController.popBackStack()
                            }
                        },
                        onCancel = {
                            if (entry.isResumed()) navController.popBackStack()
                        }
                    )
                }
            }

            composable(
                route = Routes.DELETE_TASK,
                arguments = listOf(navArgument(Routes.ARG_TASK_ID) { type = NavType.IntType })
            ) { entry ->
                val taskId = entry.arguments?.getInt(Routes.ARG_TASK_ID) ?: 0
                val task = viewModel.task(taskId)

                if (task != null) {
                    DeleteTaskScreen(
                        task = task,
                        onCancel = {
                            if (entry.isResumed()) navController.popBackStack()
                        },
                        onConfirmDelete = {
                            if (entry.isResumed()) {
                                viewModel.deleteTask(task.id)
                                // Go back to Today (the deleted task's screens are removed from the back stack).
                                navController.popBackStack(Routes.TODAY, inclusive = false)
                            }
                        }
                    )
                }
            }

            // ---------- Assignment screens (argument: assignmentId) ----------

            composable(
                route = Routes.ASSIGNMENT_DETAILS,
                arguments = listOf(navArgument(Routes.ARG_ASSIGNMENT_ID) { type = NavType.IntType })
            ) { entry ->
                val assignmentId = entry.arguments?.getInt(Routes.ARG_ASSIGNMENT_ID) ?: 0
                val assignment = viewModel.assignment(assignmentId)

                if (assignment != null) {
                    AssignmentDetailsScreen(
                        assignment = assignment,
                        tasks = viewModel.tasksFor(assignment.id),
                        onBack = {
                            if (entry.isResumed()) navController.popBackStack()
                        },
                        onOpenTask = { taskId ->
                            if (entry.isResumed()) {
                                navController.navigate(Routes.taskDetails(taskId)) {
                                    launchSingleTop = true
                                }
                            }
                        },
                        onToggleTask = { taskId -> viewModel.toggleCompleted(taskId) },
                        onDeleteAssignment = {
                            if (entry.isResumed()) navController.navigate(Routes.deleteAssignment(assignment.id))
                        }
                    )
                }
            }

            composable(
                route = Routes.DELETE_ASSIGNMENT,
                arguments = listOf(navArgument(Routes.ARG_ASSIGNMENT_ID) { type = NavType.IntType })
            ) { entry ->
                val assignmentId = entry.arguments?.getInt(Routes.ARG_ASSIGNMENT_ID) ?: 0
                val assignment = viewModel.assignment(assignmentId)

                if (assignment != null) {
                    DeleteAssignmentScreen(
                        assignment = assignment,
                        taskCount = viewModel.tasksFor(assignment.id).size,
                        onCancel = {
                            if (entry.isResumed()) navController.popBackStack()
                        },
                        onConfirmDelete = {
                            if (entry.isResumed()) {
                                viewModel.deleteAssignment(assignment.id)
                                navController.popBackStack(Routes.TODAY, inclusive = false)
                            }
                        }
                    )
                }
            }

            // ---------- Plan a new assignment (2 steps) ----------

            composable(Routes.NEW_ASSIGNMENT) { entry ->
                NewAssignmentScreen(
                    onBack = {
                        if (entry.isResumed()) navController.popBackStack()
                    },
                    onContinue = { title, subject, deadline ->
                        if (entry.isResumed()) navController.navigate(Routes.addTasks(title, subject, deadline))
                    }
                )
            }

            composable(
                route = Routes.ADD_TASKS,
                arguments = listOf(
                    navArgument(Routes.ARG_TITLE) { type = NavType.StringType },
                    navArgument(Routes.ARG_SUBJECT) { type = NavType.StringType },
                    navArgument(Routes.ARG_DEADLINE) { type = NavType.LongType }
                )
            ) { entry ->
                val title = entry.arguments?.getString(Routes.ARG_TITLE) ?: ""
                val subject = entry.arguments?.getString(Routes.ARG_SUBJECT) ?: ""
                val deadline = LocalDate.ofEpochDay(entry.arguments?.getLong(Routes.ARG_DEADLINE) ?: 0L)

                AddTasksScreen(
                    assignmentTitle = title,
                    deadline = deadline,
                    onBack = {
                        if (entry.isResumed()) navController.popBackStack()
                    },
                    onSavePlan = { drafts ->
                        if (entry.isResumed()) {
                            viewModel.createAssignment(title, subject, deadline, drafts)
                            // Show the new tasks on Upcoming. Back from there goes to Today, not the form.
                            navController.navigate(Routes.UPCOMING) {
                                popUpTo(Routes.TODAY)
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
        }
    }
}

// True only when this screen is fully shown.
// We check this before navigating, so fast double taps can't open the same screen twice
// (the second tap happens while the screen is already changing, so it is ignored).
private fun NavBackStackEntry.isResumed(): Boolean =
    lifecycle.currentState == Lifecycle.State.RESUMED

// Switches bottom bar tabs without stacking the same tab screen again.
private fun goToTab(navController: NavHostController, route: String) {
    navController.navigate(route) {
        popUpTo(Routes.TODAY) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
