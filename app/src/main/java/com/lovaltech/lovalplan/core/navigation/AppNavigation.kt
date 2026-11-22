package com.lovaltech.lovalplan.core.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.lovaltech.lovalplan.R
import com.lovaltech.lovalplan.core.designsystem.theme.Night
import com.lovaltech.lovalplan.feature.auth.presentation.screen.LoginScreen
import com.lovaltech.lovalplan.feature.auth.presentation.viewmodel.SessionState
import com.lovaltech.lovalplan.feature.auth.presentation.viewmodel.SessionViewModel
import com.lovaltech.lovalplan.feature.dashboard.presentation.screen.DashboardScreen
import com.lovaltech.lovalplan.feature.profile.presentation.screen.ProfileScreen
import com.lovaltech.lovalplan.feature.progress.presentation.screen.AchievementsScreen
import com.lovaltech.lovalplan.feature.schedule.presentation.screen.EventFormScreen
import com.lovaltech.lovalplan.feature.schedule.presentation.screen.ScheduleScreen
import java.time.LocalDate

/**
 * Raiz de la UI. Primero decide si mostrar Login o el Dashboard (SessionViewModel)
 * y luego arma la pila de Navigation 3 con la barra inferior.
 */
@Composable
fun AppNavigation(sessionVm: SessionViewModel = hiltViewModel()) {
    val session by sessionVm.state.collectAsStateWithLifecycle()
    when (session) {
        SessionState.Loading -> Splash()
        SessionState.NeedsLogin -> LovalNavHost(start = LoginRoute)
        SessionState.Ready -> LovalNavHost(start = DashboardRoute)
    }
}

@Composable
private fun Splash() {
    Box(Modifier.fillMaxSize().background(Night), contentAlignment = Alignment.Center) {
        Image(painterResource(R.drawable.ic_dragon), contentDescription = null)
    }
}

@Composable
private fun LovalNavHost(start: NavKey) {
    val backStack = rememberNavBackStack(start)
    val current = backStack.lastOrNull()
    val showBottomBar = TopLevelTabs.any { it.route == current }

    Scaffold(
        // La barra inferior maneja sus propios insets; cada pantalla maneja los suyos.
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (showBottomBar) LovalBottomBar(current) { tab -> backStack.goToTab(tab) }
        }
    ) { padding ->
        NavDisplay(
            backStack = backStack,
            modifier = Modifier.padding(padding).consumeWindowInsets(padding),
            onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<LoginRoute> {
                    LoginScreen(
                        vm = hiltViewModel(),
                        onSignedIn = { backStack.resetTo(DashboardRoute) }
                    )
                }
                entry<DashboardRoute> {
                    DashboardScreen(
                        vm = hiltViewModel(),
                        onAddEvent = { backStack.add(EventFormRoute(dayEpoch = LocalDate.now().toEpochDay())) },
                        onOpenEvent = { id -> backStack.add(EventFormRoute(eventId = id)) }
                    )
                }
                entry<ScheduleRoute> {
                    ScheduleScreen(
                        vm = hiltViewModel(),
                        onAddEvent = { day -> backStack.add(EventFormRoute(dayEpoch = day)) },
                        onEditEvent = { id -> backStack.add(EventFormRoute(eventId = id)) }
                    )
                }
                entry<AchievementsRoute> {
                    AchievementsScreen(vm = hiltViewModel())
                }
                entry<ProfileRoute> {
                    ProfileScreen(
                        vm = hiltViewModel(),
                        onSignedOut = { backStack.resetTo(LoginRoute) }
                    )
                }
                entry<EventFormRoute> { route ->
                    EventFormScreen(
                        vm = hiltViewModel(),
                        eventId = route.eventId,
                        dayEpoch = route.dayEpoch,
                        onDone = { backStack.removeLastOrNull() }
                    )
                }
            }
        )
    }
}

/** Pestanas: el Dashboard siempre queda en la base para que "Atras" vuelva a Inicio. */
private fun MutableList<NavKey>.goToTab(tab: NavKey) {
    if (lastOrNull() == tab) return
    clear()
    add(DashboardRoute)
    if (tab != DashboardRoute) add(tab)
}

private fun MutableList<NavKey>.resetTo(route: NavKey) {
    clear()
    add(route)
}
