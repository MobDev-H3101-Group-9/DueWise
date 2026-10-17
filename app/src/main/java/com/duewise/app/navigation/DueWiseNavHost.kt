package com.duewise.app.navigation

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
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

/**
 * App shell: one NavController, one Scaffold, and the bottom bar on the four tab screens.
 * All navigation logic lives in this file; screens only report user events through lambdas.
 */
@Composable
fun DueWiseApp(viewModel: DueWiseViewModel) {
    val navController = rememberNavController()
    val currentEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentEntry?.destination?.route
    val showBottomBar = TopLevelTab.entries.any { it.route == currentRoute }

    Scaffold(
        containerColor = AppBackground,
        bottomBar = {
            if (showBottomBar) {
                DueWiseBottomBar(
                    currentRoute = currentRoute,
                    onTabSelected = { route -> navController.navigateToTab(route) }
                )
            }
        }
    ) { innerPadding ->
        DueWiseNavHost(
            navController = navController,
            viewModel = viewModel,
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        )
    }
}

/** Every screen is declared here, inside a single NavHost. The app starts at Today (decision A9). */
@Composable
private fun DueWiseNavHost(
    navController: NavHostController,
    viewModel: DueWiseViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.TODAY,
        modifier = modifier
    ) {

        // ---------- Bottom-navigation tabs ----------

        composable(Routes.TODAY) { entry ->
            TodayScreen(
                today = LocalDate.now(),
                nextTasks = viewModel.nextPendingTasks(),
                completedTasks = viewModel.completedTasks(),
                assignmentTitles = viewModel.assignmentTitles(),
                onAddAssignment = { entry.ifCurrent { navController.navigate(Routes.NEW_ASSIGNMENT) } },
                onOpenNotifications = { entry.ifCurrent { navController.navigate(Routes.NOTIFICATIONS) } },
                onSeeAllUpcoming = { entry.ifCurrent { navController.navigateToTab(Routes.UPCOMING) } },
                onOpenTask = { taskId -> entry.ifCurrent { navController.navigate(Routes.taskDetails(taskId)) } },
                onToggleTask = viewModel::toggleCompleted
            )
        }

        composable(Routes.UPCOMING) { entry ->
            UpcomingScreen(
                tasks = viewModel.pendingTasks(),
                assignmentTitles = viewModel.assignmentTitles(),
                onOpenTask = { taskId -> entry.ifCurrent { navController.navigate(Routes.taskDetails(taskId)) } },
                onToggleTask = viewModel::toggleCompleted
            )
        }

        composable(Routes.COMPLETED) { entry ->
            CompletedScreen(
                tasks = viewModel.completedTasks(),
                assignmentTitles = viewModel.assignmentTitles(),
                onOpenTask = { taskId -> entry.ifCurrent { navController.navigate(Routes.taskDetails(taskId)) } },
                onToggleTask = viewModel::toggleCompleted
            )
        }

        composable(Routes.SETTINGS) { entry ->
            SettingsScreen(
                onLogOut = {
                    entry.ifCurrent {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(navController.graph.id) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        // ---------- Sign in (reached after Log Out) ----------

        composable(Routes.LOGIN) { entry ->
            LoginScreen(
                onSignIn = {
                    entry.ifCurrent {
                        navController.navigate(Routes.TODAY) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        // ---------- Notifications ----------

        composable(Routes.NOTIFICATIONS) { entry ->
            NotificationsScreen(
                reminders = viewModel.reminders(),
                onBack = { entry.ifCurrent { navController.popBackStack() } },
                onOpenTask = { taskId -> entry.ifCurrent { navController.navigate(Routes.taskDetails(taskId)) } }
            )
        }

        // ---------- Task screens ----------

        composable(
            route = Routes.TASK_DETAILS,
            arguments = listOf(navArgument(Routes.ARG_TASK_ID) { type = NavType.IntType })
        ) { entry ->
            val taskId = entry.arguments?.getInt(Routes.ARG_TASK_ID) ?: return@composable
            val task = viewModel.task(taskId) ?: return@composable // deleted while this screen is leaving
            val assignment = viewModel.assignment(task.assignmentId) ?: return@composable

            TaskDetailsScreen(
                task = task,
                assignment = assignment,
                onBack = { entry.ifCurrent { navController.popBackStack() } },
                onOpenAssignment = {
                    entry.ifCurrent {
                        // Came here from this assignment's details? Go back instead of stacking it again.
                        if (navController.previousIs(Routes.ASSIGNMENT_DETAILS, Routes.ARG_ASSIGNMENT_ID, assignment.id)) {
                            navController.popBackStack()
                        } else {
                            navController.navigate(Routes.assignmentDetails(assignment.id))
                        }
                    }
                },
                onToggleCompleted = { viewModel.toggleCompleted(task.id) },
                onEdit = { entry.ifCurrent { navController.navigate(Routes.editTask(task.id)) } },
                onDelete = { entry.ifCurrent { navController.navigate(Routes.deleteTask(task.id)) } }
            )
        }

        composable(
            route = Routes.EDIT_TASK,
            arguments = listOf(navArgument(Routes.ARG_TASK_ID) { type = NavType.IntType })
        ) { entry ->
            val taskId = entry.arguments?.getInt(Routes.ARG_TASK_ID) ?: return@composable
            val task = viewModel.task(taskId) ?: return@composable
            val assignment = viewModel.assignment(task.assignmentId) ?: return@composable

            EditTaskScreen(
                task = task,
                assignment = assignment,
                onSave = { name, targetDate ->
                    entry.ifCurrent {
                        viewModel.updateTask(task.id, name, targetDate)
                        navController.popBackStack()
                    }
                },
                onCancel = { entry.ifCurrent { navController.popBackStack() } }
            )
        }

        composable(
            route = Routes.DELETE_TASK,
            arguments = listOf(navArgument(Routes.ARG_TASK_ID) { type = NavType.IntType })
        ) { entry ->
            val taskId = entry.arguments?.getInt(Routes.ARG_TASK_ID) ?: return@composable
            val task = viewModel.task(taskId) ?: return@composable

            DeleteTaskScreen(
                task = task,
                onCancel = { entry.ifCurrent { navController.popBackStack() } },
                onConfirmDelete = {
                    entry.ifCurrent {
                        viewModel.deleteTask(task.id)
                        navController.popBackStack(Routes.TODAY, inclusive = false)
                    }
                }
            )
        }

        // ---------- Assignment screens ----------

        composable(
            route = Routes.ASSIGNMENT_DETAILS,
            arguments = listOf(navArgument(Routes.ARG_ASSIGNMENT_ID) { type = NavType.IntType })
        ) { entry ->
            val assignmentId = entry.arguments?.getInt(Routes.ARG_ASSIGNMENT_ID) ?: return@composable
            val assignment = viewModel.assignment(assignmentId) ?: return@composable

            AssignmentDetailsScreen(
                assignment = assignment,
                tasks = viewModel.tasksFor(assignment.id),
                onBack = { entry.ifCurrent { navController.popBackStack() } },
                onOpenTask = { taskId ->
                    entry.ifCurrent {
                        // Came here from this task's details? Go back instead of stacking it again.
                        if (navController.previousIs(Routes.TASK_DETAILS, Routes.ARG_TASK_ID, taskId)) {
                            navController.popBackStack()
                        } else {
                            navController.navigate(Routes.taskDetails(taskId))
                        }
                    }
                },
                onToggleTask = viewModel::toggleCompleted,
                onDeleteAssignment = { entry.ifCurrent { navController.navigate(Routes.deleteAssignment(assignment.id)) } }
            )
        }

        composable(
            route = Routes.DELETE_ASSIGNMENT,
            arguments = listOf(navArgument(Routes.ARG_ASSIGNMENT_ID) { type = NavType.IntType })
        ) { entry ->
            val assignmentId = entry.arguments?.getInt(Routes.ARG_ASSIGNMENT_ID) ?: return@composable
            val assignment = viewModel.assignment(assignmentId) ?: return@composable

            DeleteAssignmentScreen(
                assignment = assignment,
                taskCount = viewModel.tasksFor(assignment.id).size,
                onCancel = { entry.ifCurrent { navController.popBackStack() } },
                onConfirmDelete = {
                    entry.ifCurrent {
                        viewModel.deleteAssignment(assignment.id)
                        navController.popBackStack(Routes.TODAY, inclusive = false)
                    }
                }
            )
        }

        // ---------- Plan a new assignment (two steps) ----------

        composable(Routes.NEW_ASSIGNMENT) { entry ->
            NewAssignmentScreen(
                defaultDeadline = LocalDate.now().plusWeeks(2),
                onBack = { entry.ifCurrent { navController.popBackStack() } },
                onContinue = { title, subject, deadline ->
                    entry.ifCurrent { navController.navigate(Routes.addTasks(title, subject, deadline)) }
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
            val arguments = entry.arguments ?: return@composable
            val title = arguments.getString(Routes.ARG_TITLE).orEmpty()
            val subject = arguments.getString(Routes.ARG_SUBJECT).orEmpty()
            val deadline = LocalDate.ofEpochDay(arguments.getLong(Routes.ARG_DEADLINE))

            AddTasksScreen(
                assignmentTitle = title,
                deadline = deadline,
                today = LocalDate.now(),
                onBack = { entry.ifCurrent { navController.popBackStack() } },
                onSavePlan = { drafts ->
                    entry.ifCurrent {
                        viewModel.createAssignment(title, subject, deadline, drafts)
                        // Show the result on Upcoming; Back from there returns to Today, not to the form.
                        navController.navigate(Routes.UPCOMING) {
                            popUpTo(Routes.TODAY) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}

/**
 * Runs a navigation action only while this screen is the one fully shown (RESUMED).
 * Taps that arrive during a screen transition are ignored, so rapid tapping cannot open
 * the same screen twice or pop the back stack more than once.
 */
private fun NavBackStackEntry.ifCurrent(action: () -> Unit) {
    if (lifecycle.currentState == Lifecycle.State.RESUMED) action()
}

/** Switches bottom-navigation tabs without stacking duplicate tab screens. */
private fun NavHostController.navigateToTab(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

/** True if the screen under the current one is [route] with the given Int argument. */
private fun NavHostController.previousIs(route: String, argName: String, argValue: Int): Boolean {
    val previous = previousBackStackEntry ?: return false
    return previous.destination.route == route && previous.arguments?.getInt(argName) == argValue
}
