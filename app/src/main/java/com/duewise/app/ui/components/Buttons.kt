package com.duewise.app.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.duewise.app.ui.theme.CardSurface
import com.duewise.app.ui.theme.PrimaryDark
import com.duewise.app.ui.theme.TextPrimary

// Dark filled button for the main action of a screen, e.g. "Continue to Tasks".
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    color: Color = PrimaryDark
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .height(56.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = CardSurface,
            disabledContainerColor = color.copy(alpha = 0.35f),
            disabledContentColor = CardSurface
        )
    ) {
        Text(text = text, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
    }
}

// White outlined button, e.g. "Edit task". Use red text only for delete actions.
@Composable
fun OutlinedActionButton(
    text: String,
    onClick: () -> Unit,
    textColor: Color = TextPrimary,
    centered: Boolean = false
) {
    DueWiseCard(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (textColor == TextPrimary) FontWeight.Normal else FontWeight.SemiBold,
            color = textColor,
            textAlign = if (centered) TextAlign.Center else TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp)
        )
    }
}
