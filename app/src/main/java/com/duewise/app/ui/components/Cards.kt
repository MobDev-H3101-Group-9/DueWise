package com.duewise.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duewise.app.data.Task
import com.duewise.app.ui.theme.AccentBlue
import com.duewise.app.ui.theme.CardBorder
import com.duewise.app.ui.theme.CardSurface
import com.duewise.app.ui.theme.TextPrimary
import com.duewise.app.ui.theme.TextSecondary
import com.duewise.app.util.toDisplayDate

private val CardShape = RoundedCornerShape(16.dp)
private val CardBorderStroke = BorderStroke(1.dp, CardBorder)

/** The white, rounded, outlined container used for cards and outlined buttons. */
@Composable
fun DueWiseCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    if (onClick != null) {
        Surface(
            onClick = onClick,
            modifier = modifier,
            shape = CardShape,
            color = CardSurface,
            border = CardBorderStroke,
            content = content
        )
    } else {
        Surface(
            modifier = modifier,
            shape = CardShape,
            color = CardSurface,
            border = CardBorderStroke,
            content = content
        )
    }
}

/**
 * The one task card used on Today, Upcoming, Completed and Assignment details (decision A1):
 * tapping the circle completes or restores the task; tapping anywhere else opens Task details.
 *
 * @param assignmentTitle parent assignment shown under the task name, or null to hide it
 *   (on Assignment details every task belongs to the same assignment).
 */
@Composable
fun TaskCard(
    task: Task,
    assignmentTitle: String?,
    onToggleCompleted: () -> Unit,
    onOpen: () -> Unit,
    modifier: Modifier = Modifier
) {
    DueWiseCard(onClick = onOpen, modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(start = 6.dp, end = 16.dp, top = 6.dp, bottom = 14.dp),
            verticalAlignment = Alignment.Top
        ) {
            CompletionToggle(
                isCompleted = task.isCompleted,
                taskName = task.name,
                onToggle = onToggleCompleted
            )
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
                    Text(
                        text = assignmentTitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
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

/** Circle that shows ○ when pending and ✓ when completed. Has a 48dp touch target. */
@Composable
fun CompletionToggle(
    isCompleted: Boolean,
    taskName: String,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .toggleable(
                value = isCompleted,
                role = Role.Checkbox,
                onValueChange = { onToggle() }
            )
            .semantics {
                contentDescription =
                    if (isCompleted) "Mark $taskName as not done" else "Mark $taskName as completed"
            },
        contentAlignment = Alignment.Center
    ) {
        if (isCompleted) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = TextPrimary,
                modifier = Modifier.size(22.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .border(width = 2.dp, color = TextPrimary, shape = CircleShape)
            )
        }
    }
}
