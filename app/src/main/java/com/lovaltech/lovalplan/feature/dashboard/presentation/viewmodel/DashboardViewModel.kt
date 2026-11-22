package com.lovaltech.lovalplan.feature.dashboard.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovaltech.lovalplan.core.sync.SyncAllUseCase
import com.lovaltech.lovalplan.feature.dashboard.presentation.state.DashboardTransient
import com.lovaltech.lovalplan.feature.dashboard.presentation.state.DashboardUiState
import com.lovaltech.lovalplan.feature.profile.domain.repository.ProfileRepository
import com.lovaltech.lovalplan.feature.progress.domain.repository.ProgressRepository
import com.lovaltech.lovalplan.feature.progress.domain.usecase.CalculateProgressUseCase
import com.lovaltech.lovalplan.feature.progress.domain.usecase.CompleteEventUseCase
import com.lovaltech.lovalplan.feature.schedule.domain.model.AgendaStatus
import com.lovaltech.lovalplan.feature.schedule.domain.repository.ScheduleRepository
import com.lovaltech.lovalplan.feature.schedule.domain.usecase.BuildDayAgendaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * El Dashboard no tiene datos propios: COMBINA las demas features.
 * 5 fuentes -> 1 estado: actividades, cumplimientos, perfil, reloj (cada 30 s) y estado temporal.
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    scheduleRepository: ScheduleRepository,
    progressRepository: ProgressRepository,
    profileRepository: ProfileRepository,
    private val buildAgenda: BuildDayAgendaUseCase,
    private val calculate: CalculateProgressUseCase,
    private val completeEvent: CompleteEventUseCase,
    private val syncAll: SyncAllUseCase,
    private val clock: Clock
) : ViewModel() {

    private val transient = MutableStateFlow(DashboardTransient())

    // "Tic" cada 30 segundos para que los estados (Proxima -> En curso) cambien solos.
    private val ticker = flow {
        while (true) {
            emit(LocalDateTime.now(clock))
            delay(30_000)
        }
    }

    val ui: StateFlow<DashboardUiState> = combine(
        scheduleRepository.observeEvents(),
        progressRepository.observeCompletions(),
        profileRepository.observeProfile(),
        ticker,
        transient
    ) { events, completions, profile, now, temp ->
        val today = buildAgenda(now.toLocalDate(), now, events, completions.map { it.id }.toSet())
        val next = today.firstOrNull { it.status == AgendaStatus.IN_PROGRESS || it.status == AgendaStatus.UPCOMING }
        DashboardUiState(
            loading = false,
            greeting = greetingFor(now.hour),
            name = profile.displayName.ifBlank { "campeón" }.substringBefore(' '),
            stats = calculate(completions, profile.dailyGoal, events.size),
            today = today,
            next = next,
            minutesToNext = next
                ?.takeIf { it.status == AgendaStatus.UPCOMING }
                ?.let { Duration.between(now, now.toLocalDate().atTime(it.event.startTime)).toMinutes() },
            isSyncing = temp.isSyncing,
            message = temp.message,
            celebration = temp.celebration
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DashboardUiState())

    init {
        sync(silent = true)
    }

    fun complete(eventId: String) = viewModelScope.launch {
        try {
            val result = completeEvent(eventId)
            transient.update { it.copy(celebration = result, message = null) }
        } catch (e: Exception) {
            transient.update { it.copy(message = e.message) }
        }
    }

    fun dismissCelebration() = transient.update { it.copy(celebration = null) }

    fun dismissMessage() = transient.update { it.copy(message = null) }

    fun sync(silent: Boolean = false) = viewModelScope.launch {
        transient.update { it.copy(isSyncing = true, message = null) }
        try {
            val report = syncAll()
            if (!silent) transient.update { it.copy(message = "Sincronizado ✓ ${report.uploaded} enviados, ${report.downloaded} recibidos") }
        } catch (e: Exception) {
            Log.w("DashboardViewModel", "Fallo al sincronizar", e)
            if (!silent) transient.update { it.copy(message = "Sin conexión: tus datos siguen guardados en el celular") }
        } finally {
            transient.update { it.copy(isSyncing = false) }
        }
    }

    private fun greetingFor(hour: Int) = when (hour) {
        in 5..11 -> "¡Buenos días"
        in 12..18 -> "¡Buenas tardes"
        else -> "¡Buenas noches"
    }
}
