package com.lovaltech.lovalplan.feature.schedule.presentation.state

import com.lovaltech.lovalplan.feature.schedule.domain.model.EventCategory
import com.lovaltech.lovalplan.feature.schedule.domain.model.RecurrenceType
import java.time.LocalDate

/** Un unico estado inmutable para todo el formulario (como ProductFormUiState). */
data class EventFormUiState(
    val title: String = "",
    val description: String = "",
    val category: EventCategory = EventCategory.STUDY,
    val recurrence: RecurrenceType = RecurrenceType.WEEKLY,
    val daysOfWeek: Set<Int> = emptySet(),
    val date: LocalDate? = null,
    val startMinute: Int = 8 * 60,
    val endMinute: Int = 9 * 60,
    val reminderMinutes: Int = 10,
    val isEditing: Boolean = false,
    val saving: Boolean = false,
    val error: String? = null,
    val saved: Boolean = false
)
