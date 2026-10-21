package com.duewise.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duewise.app.navigation.DueWiseApp
import com.duewise.app.ui.DueWiseViewModel
import com.duewise.app.ui.theme.DueWiseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DueWiseTheme {
                val viewModel: DueWiseViewModel = viewModel()
                DueWiseApp(viewModel = viewModel)
            }
        }
    }
}
