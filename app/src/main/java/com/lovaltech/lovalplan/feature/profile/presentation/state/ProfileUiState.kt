package com.lovaltech.lovalplan.feature.profile.presentation.state

import com.lovaltech.lovalplan.feature.progress.domain.model.ProgressStats

data class ProfileUiState(
    val name: String = "",
    val email: String? = null,
    val isGuest: Boolean = true,
    val dailyGoal: Int = 1,
    val stats: ProgressStats = ProgressStats(),
    val isSyncing: Boolean = false,
    val message: String? = null,
    val signedOut: Boolean = false
)

/** Estado de los permisos que necesitan las alarmas (lo lee la Screen al volver a la app). */
data class PermissionStatus(
    val notifications: Boolean = true,
    val exactAlarms: Boolean = true,
    val fullScreen: Boolean = true
)

internal data class ProfileTransient(
    val isSyncing: Boolean = false,
    val message: String? = null,
    val signedOut: Boolean = false
)
