package com.lovaltech.lovalplan.feature.schedule.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovaltech.lovalplan.feature.schedule.domain.model.EventCategory
import com.lovaltech.lovalplan.feature.schedule.domain.model.RecurrenceType
import com.lovaltech.lovalplan.feature.schedule.domain.model.ScheduleEvent
import com.lovaltech.lovalplan.feature.schedule.domain.repository.ScheduleRepository
import com.lovaltech.lovalplan.feature.schedule.domain.usecase.DeleteEventUseCase
import com.lovaltech.lovalplan.feature.schedule.domain.usecase.SaveEventUseCase
import com.lovaltech.lovalplan.feature.schedule.presentation.state.EventFormUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

/** Mismo patron que ProductFormViewModel: load -> updateX -> save. */
@HiltViewModel
class EventFormViewModel @Inject constructor(
    private val repository: ScheduleRepository,
    private val saveEvent: SaveEventUseCase,
    private val deleteEvent: DeleteEventUseCase
) : ViewModel() {
    private val _ui = MutableStateFlow(EventFormUiState())
    val ui: StateFlow<EventFormUiState> = _ui
    private var editingId: String? = null
    private var loaded = false

    fun load(eventId: String?, dayEpoch: Long?) {
        if (loaded) return
        loaded = true
        editingId = eventId
        if (eventId == null) {
            // Nueva actividad: se preselecciona el dia desde el que se abrio el formulario.
            val day = dayEpoch?.let(LocalDate::ofEpochDay) ?: LocalDate.now()
            _ui.value = EventFormUiState(daysOfWeek = setOf(day.dayOfWeek.value), date = day)
            return
        }
        viewModelScope.launch {
            val event = repository.findById(eventId) ?: return@launch
            _ui.value = EventFormUiState(
                title = event.title,
                description = event.description,
                category = event.category,
                recurrence = event.recurrence,
                daysOfWeek = event.daysOfWeek,
                date = event.date ?: LocalDate.now(),
                startMinute = event.startMinute,
                endMinute = event.endMinute,
                reminderMinutes = event.reminderMinutes,
                isEditing = true
            )
        }
    }

    fun updateTitle(v: String) = _ui.update { it.copy(title = v, error = null) }
    fun updateDescription(v: String) = _ui.update { it.copy(description = v) }
    fun updateCategory(v: EventCategory) = _ui.update { it.copy(category = v) }
    fun updateRecurrence(v: RecurrenceType) = _ui.update { it.copy(recurrence = v, error = null) }
    fun toggleDay(day: Int) = _ui.update {
        it.copy(daysOfWeek = if (day in it.daysOfWeek) it.daysOfWeek - day else it.daysOfWeek + day, error = null)
    }
    fun updateDate(v: LocalDate) = _ui.update { it.copy(date = v, error = null) }
    fun updateReminder(v: Int) = _ui.update { it.copy(reminderMinutes = v) }

    /** Al mover el inicio, el fin se corre para conservar la duracion. */
    fun updateStart(minute: Int) = _ui.update {
        val duration = (it.endMinute - it.startMinute).coerceAtLeast(30)
        it.copy(startMinute = minute, endMinute = (minute + duration).coerceAtMost(1439), error = null)
    }
    fun updateEnd(minute: Int) = _ui.update { it.copy(endMinute = minute, error = null) }

    fun save() = viewModelScope.launch {
        val current = _ui.value
        _ui.value = current.copy(saving = true, error = null)
        try {
            saveEvent(
                ScheduleEvent(
                    id = editingId ?: UUID.randomUUID().toString(),
                    title = current.title.trim(),
                    description = current.description.trim(),
                    category = current.category,
                    recurrence = current.recurrence,
                    daysOfWeek = if (current.recurrence == RecurrenceType.WEEKLY) current.daysOfWeek else emptySet(),
                    date = if (current.recurrence == RecurrenceType.ONCE) current.date else null,
                    startMinute = current.startMinute,
                    endMinute = current.endMinute,
                    reminderMinutes = current.reminderMinutes
                )
            )
            // Guardado local exitoso (y alarma programada), aunque no haya Internet.
            _ui.value = _ui.value.copy(saving = false, saved = true)
        } catch (e: Exception) {
            _ui.value = _ui.value.copy(saving = false, error = e.message ?: "No se pudo guardar")
        }
    }

    fun delete() = viewModelScope.launch {
        val id = editingId ?: return@launch
        deleteEvent(id)
        _ui.value = _ui.value.copy(saved = true)
    }
}
