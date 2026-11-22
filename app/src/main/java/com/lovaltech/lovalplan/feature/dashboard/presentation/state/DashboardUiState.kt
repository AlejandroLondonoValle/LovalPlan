package com.lovaltech.lovalplan.feature.dashboard.presentation.state

import com.lovaltech.lovalplan.feature.progress.domain.model.CompletionResult
import com.lovaltech.lovalplan.feature.progress.domain.model.ProgressStats
import com.lovaltech.lovalplan.feature.schedule.domain.model.AgendaItem
import com.lovaltech.lovalplan.feature.schedule.domain.model.AgendaStatus

/** Todo lo que pinta el Dashboard en un solo objeto inmutable. */
data class DashboardUiState(
    val loading: Boolean = true,
    val greeting: String = "",
    val name: String = "",
    val stats: ProgressStats = ProgressStats(),
    val today: List<AgendaItem> = emptyList(),
    val next: AgendaItem? = null,
    val minutesToNext: Long? = null,
    val isSyncing: Boolean = false,
    val message: String? = null,
    val celebration: CompletionResult? = null
) {
    val completedToday: Int get() = today.count { it.status == AgendaStatus.COMPLETED }
    val todayProgress: Float get() = if (today.isEmpty()) 0f else completedToday.toFloat() / today.size
}

/** Parte del estado que no viene de Room: sincronizacion, mensajes y celebracion. */
data class DashboardTransient(
    val isSyncing: Boolean = false,
    val message: String? = null,
    val celebration: CompletionResult? = null
)
