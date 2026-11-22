package com.lovaltech.lovalplan.core.navigation

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation3.runtime.NavKey
import com.lovaltech.lovalplan.R
import com.lovaltech.lovalplan.core.designsystem.theme.LovalBlue
import com.lovaltech.lovalplan.core.designsystem.theme.LovalBlueLight
import com.lovaltech.lovalplan.core.designsystem.theme.NightSurface
import com.lovaltech.lovalplan.core.designsystem.theme.TextSecondary

data class TopLevelTab(val route: NavKey, val label: String, @DrawableRes val icon: Int)

val TopLevelTabs = listOf(
    TopLevelTab(DashboardRoute, "Inicio", R.drawable.ic_home),
    TopLevelTab(ScheduleRoute, "Horario", R.drawable.ic_calendar),
    TopLevelTab(AchievementsRoute, "Logros", R.drawable.ic_trophy),
    TopLevelTab(ProfileRoute, "Perfil", R.drawable.ic_person)
)

/** Barra inferior con las 4 secciones de la app. */
@Composable
fun LovalBottomBar(current: NavKey?, onSelect: (NavKey) -> Unit) {
    NavigationBar(containerColor = NightSurface) {
        TopLevelTabs.forEach { tab ->
            NavigationBarItem(
                selected = current == tab.route,
                onClick = { onSelect(tab.route) },
                icon = { Icon(painterResource(tab.icon), contentDescription = tab.label) },
                label = { Text(tab.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = LovalBlueLight,
                    indicatorColor = LovalBlue,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary
                )
            )
        }
    }
}
