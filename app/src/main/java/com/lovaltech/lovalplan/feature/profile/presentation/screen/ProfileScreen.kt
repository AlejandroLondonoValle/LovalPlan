package com.lovaltech.lovalplan.feature.profile.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lovaltech.lovalplan.core.designsystem.theme.Night
import com.lovaltech.lovalplan.feature.auth.presentation.google.GoogleIdTokenRequester
import com.lovaltech.lovalplan.feature.auth.presentation.google.GoogleTokenResult
import com.lovaltech.lovalplan.feature.profile.presentation.composables.EditNameDialog
import com.lovaltech.lovalplan.feature.profile.presentation.composables.SignOutDialog
import com.lovaltech.lovalplan.feature.profile.presentation.section.AccountSection
import com.lovaltech.lovalplan.feature.profile.presentation.section.DailyGoalSection
import com.lovaltech.lovalplan.feature.profile.presentation.section.PermissionsSection
import com.lovaltech.lovalplan.feature.profile.presentation.section.ProfileHeaderSection
import com.lovaltech.lovalplan.feature.profile.presentation.state.PermissionStatus
import com.lovaltech.lovalplan.feature.profile.presentation.viewmodel.ProfileViewModel
import com.lovaltech.lovalplan.feature.reminder.presentation.permissions.ReminderPermissions
import kotlinx.coroutines.launch

/** Pantalla "Perfil". */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    vm: ProfileViewModel,
    onSignedOut: () -> Unit
) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }
    var editingName by remember { mutableStateOf(false) }
    var askSignOut by remember { mutableStateOf(false) }
    var permissions by remember { mutableStateOf(PermissionStatus()) }

    // Cada vez que el usuario vuelve de Ajustes se relee el estado de los permisos.
    LifecycleResumeEffect(Unit) {
        permissions = PermissionStatus(
            notifications = ReminderPermissions.notificationsGranted(context),
            exactAlarms = ReminderPermissions.exactAlarmsGranted(context),
            fullScreen = ReminderPermissions.fullScreenGranted(context)
        )
        onPauseOrDispose { }
    }
    LaunchedEffect(ui.signedOut) {
        if (ui.signedOut) {
            GoogleIdTokenRequester.clear(context)
            onSignedOut()
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
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Perfil") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Night)
            )
        }
    ) { padding ->
        LazyColumn(
            Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ProfileHeaderSection(
                    name = ui.name,
                    email = ui.email,
                    isGuest = ui.isGuest,
                    stats = ui.stats,
                    onEditName = { editingName = true }
                )
            }
            item { DailyGoalSection(goal = ui.dailyGoal, onSelect = vm::updateDailyGoal) }
            item {
                PermissionsSection(
                    status = permissions,
                    onEnableNotifications = { ReminderPermissions.openNotificationSettings(context) },
                    onEnableExactAlarms = { ReminderPermissions.openExactAlarmSettings(context) },
                    onEnableFullScreen = { ReminderPermissions.openFullScreenSettings(context) }
                )
            }
            item {
                AccountSection(
                    isGuest = ui.isGuest,
                    isSyncing = ui.isSyncing,
                    onSync = vm::sync,
                    onLinkGoogle = {
                        scope.launch {
                            when (val result = GoogleIdTokenRequester.request(context)) {
                                is GoogleTokenResult.Success -> vm.linkGoogle(result.idToken)
                                is GoogleTokenResult.Failure -> vm.showMessage(result.message)
                            }
                        }
                    },
                    onSignOut = { askSignOut = true }
                )
            }
        }
    }

    if (editingName) EditNameDialog(
        current = ui.name,
        onConfirm = {
            vm.updateName(it)
            editingName = false
        },
        onDismiss = { editingName = false }
    )
    if (askSignOut) SignOutDialog(
        isGuest = ui.isGuest,
        onConfirm = {
            askSignOut = false
            vm.signOut()
        },
        onDismiss = { askSignOut = false }
    )
}
