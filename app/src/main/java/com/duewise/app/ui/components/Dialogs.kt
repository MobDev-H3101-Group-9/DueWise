package com.duewise.app.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.duewise.app.ui.theme.CardSurface
import com.duewise.app.ui.theme.DangerRed
import com.duewise.app.ui.theme.TextPrimary

// Simple "Are you sure?" dialog. The screen that uses it decides when it is shown.
@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CardSurface,
        title = { Text(title, color = TextPrimary) },
        text = { Text(message, color = TextPrimary) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(confirmText, color = DangerRed, fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextPrimary)
            }
        }
    )
}
