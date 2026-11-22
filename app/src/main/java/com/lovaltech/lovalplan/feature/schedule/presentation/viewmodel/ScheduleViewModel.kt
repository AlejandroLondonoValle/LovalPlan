package com.lovaltech.lovalplan.feature.schedule.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovaltech.lovalplan.core.util.TimeText
import com.lovaltech.lovalplan.feature.progress.domain.repository.ProgressRepository
import com.lovaltech.lovalplan.feature.schedule.domain.repository.ScheduleRepository
import com.lovaltech.lovalplan.feature.schedule.domain.usecase.BuildDayAgendaUseCase
import com.lovaltech.lovalplan.feature.schedule.domain.usecase.DeleteEventUseCase
import com.lovaltech.lovalplan.feature.schedule.presentation.state.ScheduleUiState
import com.lovaltech.lovalplan.feature.schedule.presentation.state.WeekDayUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * Horario semanal. Combina 3 fuentes reactivas: actividades (Room), cumplimientos (Room)
 * y el dia seleccionado. Cualquier cambio vuelve a calcular el estado.
 */
@HiltViewModel
class ScheduleViewModel @Inject constructor(
    scheduleRepository: ScheduleRepository,
    progressRepository: ProgressRepository,
    private val buildAgenda: BuildDayAgendaUseCase,
    private val deleteEvent: DeleteEventUseCase,
    private val clock: Clock
) : ViewModel() {

    private val selectedDay = MutableStateFlow(LocalDate.now(clock))

    val ui: StateFlow<ScheduleUiState> = combine(
        scheduleRepository.observeEvents(),
        progressRepository.observeCompletions(),
        selectedDay
    ) { events, completions, selected ->
        val today = LocalDate.now(clock)
        val monday = selected.with(DayOfWeek.MONDAY)
        val week = (0L..6L).map { offset ->
            val date = monday.plusDays(offset)
            WeekDayUi(
                date = date,
                initial = TimeText.dayInitial(date.dayOfWeek.value),
                dayNumber = date.dayOfMonth,
                isToday = date == today,
                isSelected = date == selected,
                hasEvents = events.any { it.occursOn(date) }
            )
        }
        ScheduleUiState(
            week = week,
            weekLabel = "${TimeText.shortDate(monday)} – ${TimeText.shortDate(monday.plusDays(6))}",
            selectedDay = selected,
            selectedLabel = if (selected == today) "Hoy · ${TimeText.longDate(selected)}" else TimeText.longDate(selected),
            agenda = buildAgenda(selected, LocalDateTime.now(clock), events, completions.map { it.id }.toSet()),
            totalEvents = events.size
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ScheduleUiState())

    fun selectDay(day: LocalDate) { selectedDay.value = day }
    fun previousWeek() { selectedDay.value = selectedDay.value.minusWeeks(1) }
    fun nextWeek() { selectedDay.value = selectedDay.value.plusWeeks(1) }
    fun goToToday() { selectedDay.value = LocalDate.now(clock) }

    fun delete(id: String) = viewModelScope.launch { deleteEvent(id) }
}
