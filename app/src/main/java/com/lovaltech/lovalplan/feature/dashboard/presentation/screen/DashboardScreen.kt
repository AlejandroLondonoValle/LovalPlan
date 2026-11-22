package com.lovaltech.lovalplan.feature.dashboard.presentation.screen

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lovaltech.lovalplan.R
import com.lovaltech.lovalplan.core.designsystem.theme.LovalBlue
import com.lovaltech.lovalplan.core.designsystem.theme.Night
import com.lovaltech.lovalplan.feature.dashboard.presentation.section.DashboardHeaderSection
import com.lovaltech.lovalplan.feature.dashboard.presentation.section.NextEventSection
import com.lovaltech.lovalplan.feature.dashboard.presentation.section.TodayProgressSection
import com.lovaltech.lovalplan.feature.dashboard.presentation.section.TodayTimelineSection
import com.lovaltech.lovalplan.feature.dashboard.presentation.viewmodel.DashboardViewModel
import com.lovaltech.lovalplan.feature.progress.presentation.composables.CelebrationDialog
import com.lovaltech.lovalplan.feature.reminder.presentation.permissions.ReminderPermissions

/** Inicio: resumen del dia, racha, siguiente actividad y linea de tiempo. */
@Composable
fun DashboardScreen(
    vm: DashboardViewModel,
    onAddEvent: () -> Unit,
    onOpenEvent: (String) -> Unit
) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Android 13+: pedir permiso de notificaciones la primera vez (sin el no suenan las alarmas).
    val notificationPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* El estado se refleja en Perfil > Permisos */ }
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            !ReminderPermissions.notificationsGranted(context)
        ) {
            notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    LaunchedEffect(ui.message) {
        ui.message?.let {
            snackbar.showSnackbar(it)
            vm.dismissMessage()
        }
    }

    Scaffold(
        containerColor = Night,
        snackbarHost = { SnackbarHost(snackbar) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddEvent, containerColor = LovalBlue, contentColor = Color.White) {
                Icon(painterResource(R.drawable.ic_add), contentDescription = "Agregar actividad")
            }
        }
    ) { padding ->
        LazyColumn(
            Modifier.padding(padding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                DashboardHeaderSection(
                    greeting = ui.greeting,
                    name = ui.name,
                    stats = ui.stats,
                    isSyncing = ui.isSyncing,
                    onSync = { vm.sync() }
                )
            }
            item {
                TodayProgressSection(
                    completed = ui.completedToday,
                    total = ui.today.size,
                    progress = ui.todayProgress,
                    stats = ui.stats
                )
            }
            item {
                NextEventSection(
                    next = ui.next,
                    minutesToNext = ui.minutesToNext,
                    hasEventsToday = ui.today.isNotEmpty(),
                    onAddEvent = onAddEvent
                )
            }
            item {
                TodayTimelineSection(
                    items = ui.today,
                    onComplete = { vm.complete(it) },
                    onOpen = onOpenEvent
                )
            }
        }
    }

    ui.celebration?.let { CelebrationDialog(result = it, onDismiss = vm::dismissCelebration) }
}
