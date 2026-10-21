package com.duewise.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duewise.app.data.Task
import com.duewise.app.ui.theme.AccentBlue
import com.duewise.app.ui.theme.CardBorder
import com.duewise.app.ui.theme.CardSurface
import com.duewise.app.ui.theme.TextPrimary
import com.duewise.app.ui.theme.TextSecondary
import com.duewise.app.util.toDisplayDate

// White rounded card with a gray border, used for cards and outlined buttons.
// If onClick is given, the whole card can be tapped.
@Composable
fun DueWiseCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    val colors = CardDefaults.cardColors(containerColor = CardSurface)
    val border = BorderStroke(1.dp, CardBorder)

    if (onClick != null) {
        Card(onClick = onClick, modifier = modifier, shape = shape, colors = colors, border = border) {
            content()
        }
    } else {
        Card(modifier = modifier, shape = shape, colors = colors, border = border) {
            content()
        }
    }
}

// The task card used on every list.
// Tapping the circle marks the task done (or not done). Tapping the rest opens Task details.
// assignmentTitle can be null to hide it (on Assignment details all tasks share one assignment).
@Composable
fun TaskCard(
    task: Task,
    assignmentTitle: String?,
    onToggleCompleted: () -> Unit,
    onOpen: () -> Unit
) {
    DueWiseCard(onClick = onOpen, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(start = 4.dp, end = 16.dp, top = 4.dp, bottom = 14.dp),
            verticalAlignment = Alignment.Top
        ) {
            IconButton(onClick = onToggleCompleted) {
                if (task.isCompleted) {
                    Icon(Icons.Filled.Check, contentDescription = "Mark as not done", tint = TextPrimary)
                } else {
                    // Empty circle for a task that is not done yet
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .border(2.dp, TextPrimary, CircleShape)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 12.dp)
            ) {
                Text(
                    text = task.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (task.isCompleted) TextSecondary else TextPrimary
                )
                if (assignmentTitle != null) {
                    Text(text = assignmentTitle, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                }
                Text(
                    text = task.targetDate.toDisplayDate(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = AccentBlue
                )
            }
        }
    }
}
