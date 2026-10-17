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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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

/**
 * Sign-in screen, reached after Log Out. Account access is deferred (decision A9), so this
 * checks only that both fields are filled in; no real authentication or account storage.
 */
@Composable
fun LoginScreen(onSignIn: () -> Unit) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var showError by rememberSaveable { mutableStateOf(false) }

    ScrollScreen {
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
        )
        LabeledTextField(
            label = "Password",
            value = password,
            onValueChange = {
                password = it
                showError = false
            },
            placeholder = "Password",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingContent = {
                TextButton(onClick = { passwordVisible = !passwordVisible }) {
                    Text(if (passwordVisible) "Hide" else "Show", color = AccentBlue, fontWeight = FontWeight.Medium)
                }
            }
        )

        if (showError) {
            Text("Enter your email and password.", style = MaterialTheme.typography.bodyMedium, color = DangerRed)
        }

        PrimaryButton(
            text = "Sign In",
            containerColor = AccentBlue,
            onClick = {
                if (email.isBlank() || password.isBlank()) showError = true else onSignIn()
            },
            modifier = Modifier.padding(top = 8.dp)
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
