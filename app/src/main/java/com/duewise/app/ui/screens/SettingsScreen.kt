package com.duewise.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duewise.app.ui.components.ConfirmDialog
import com.duewise.app.ui.components.DueWiseCard
import com.duewise.app.ui.components.OutlinedActionButton
import com.duewise.app.ui.components.ScreenHeader
import com.duewise.app.ui.components.ScrollScreen
import com.duewise.app.ui.components.SectionLabel
import com.duewise.app.ui.theme.DangerRed
import com.duewise.app.ui.theme.TextPrimary
import com.duewise.app.ui.theme.TextSecondary

/** Settings tab: account and app information, plus Log Out (with a confirmation dialog). */
@Composable
fun SettingsScreen(onLogOut: () -> Unit) {
    var showLogOutDialog by rememberSaveable { mutableStateOf(false) }

    ScrollScreen {
        ScreenHeader(title = "Settings")
        Text(
            text = "Your account and app information",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )

        SectionLabel("Account")
        DueWiseCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Student", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                Text("student@email.com", style = MaterialTheme.typography.bodyLarge, color = TextSecondary)
                Text("Personal assignment planner", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            }
        }

        SectionLabel("About DueWise")
        Text(
            text = "Plan early. Finish wisely.",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )
        Text(
            text = "Break assignments into smaller tasks, set target dates, and track your progress.",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )

        OutlinedActionButton(
            text = "Log Out",
            onClick = { showLogOutDialog = true },
            textColor = DangerRed,
            centered = true,
            modifier = Modifier.padding(top = 8.dp)
        )
    }

    if (showLogOutDialog) {
        ConfirmDialog(
            title = "Log out?",
            message = "You will return to the sign-in screen.",
            confirmText = "Log Out",
            onConfirm = {
                showLogOutDialog = false
                onLogOut()
            },
            onDismiss = { showLogOutDialog = false }
        )
    }
}
