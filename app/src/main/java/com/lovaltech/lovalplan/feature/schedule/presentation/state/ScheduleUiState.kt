package com.lovaltech.lovalplan.feature.schedule.presentation.state

import com.lovaltech.lovalplan.feature.schedule.domain.model.AgendaItem
import java.time.LocalDate

/** Un dia de la tira semanal. */
data class WeekDayUi(
    val date: LocalDate,
    val initial: String,
    val dayNumber: Int,
    val isToday: Boolean,
    val isSelected: Boolean,
    val hasEvents: Boolean
)

data class ScheduleUiState(
    val week: List<WeekDayUi> = emptyList(),
    val weekLabel: String = "",
    val selectedDay: LocalDate = LocalDate.now(),
    val selectedLabel: String = "",
    val agenda: List<AgendaItem> = emptyList(),
    val totalEvents: Int = 0
)
