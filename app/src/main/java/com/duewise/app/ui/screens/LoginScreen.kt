package com.duewise.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.duewise.app.ui.components.AppLogo
import com.duewise.app.ui.components.LabeledTextField
import com.duewise.app.ui.components.PrimaryButton
import com.duewise.app.ui.components.ScrollScreen
import com.duewise.app.ui.theme.AccentBlue
import com.duewise.app.ui.theme.DangerRed
import com.duewise.app.ui.theme.TextPrimary
import com.duewise.app.ui.theme.TextSecondary

// Sign-in screen, shown after Log Out.
// Real login is not part of our scope yet, so this only checks that both fields are filled in.
@Composable
fun LoginScreen(onSignIn: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    ScrollScreen {
        // Logo and app name
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AppLogo(size = 64.dp)
            Text("DueWise", style = MaterialTheme.typography.headlineMedium, color = TextPrimary)
            Text("Plan early. Finish wisely.", style = MaterialTheme.typography.bodyLarge, color = TextSecondary)
        }

        Text("Welcome back", style = MaterialTheme.typography.headlineMedium, color = TextPrimary)
        Text(
            text = "Sign in to manage your assignments and tasks.",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )

        LabeledTextField(
            label = "Email",
            value = email,
            onValueChange = {
                email = it
                showError = false
            },
            placeholder = "student@email.com",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        LabeledTextField(
            label = "Password",
            value = password,
            onValueChange = {
                password = it
                showError = false
            },
            placeholder = "Password",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingContent = {
                TextButton(onClick = { showPassword = !showPassword }) {
                    Text(if (showPassword) "Hide" else "Show", color = AccentBlue)
                }
            }
        )

        if (showError) {
            Text("Enter your email and password.", style = MaterialTheme.typography.bodyMedium, color = DangerRed)
        }

        PrimaryButton(
            text = "Sign In",
            color = AccentBlue,
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    showError = true
                } else {
                    onSignIn()
                }
            }
        )

        Text(
            text = "For individual college students",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
    }
}
