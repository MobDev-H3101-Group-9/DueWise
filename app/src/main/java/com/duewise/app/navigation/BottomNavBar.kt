package com.duewise.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.duewise.app.ui.theme.AccentBlue
import com.duewise.app.ui.theme.CardSurface
import com.duewise.app.ui.theme.TextSecondary

// The 4 tabs in the bottom bar.
enum class BottomTab(val route: String, val label: String, val icon: ImageVector) {
    Today(Routes.TODAY, "Today", Icons.Outlined.Home),
    Upcoming(Routes.UPCOMING, "Upcoming", Icons.Outlined.DateRange),
    Completed(Routes.COMPLETED, "Completed", Icons.Outlined.Check),
    Settings(Routes.SETTINGS, "Settings", Icons.Outlined.Settings)
}

@Composable
fun BottomNavBar(currentRoute: String?, onTabSelected: (String) -> Unit) {
    NavigationBar(containerColor = CardSurface) {
        for (tab in BottomTab.entries) {
            NavigationBarItem(
                selected = currentRoute == tab.route,
                onClick = { onTabSelected(tab.route) },
                icon = { Icon(tab.icon, contentDescription = null) },
                label = { Text(tab.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AccentBlue,
                    selectedTextColor = AccentBlue,
                    indicatorColor = AccentBlue.copy(alpha = 0.12f),
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary
                )
            )
        }
    }
}
