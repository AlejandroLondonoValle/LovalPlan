package com.lovaltech.lovalplan.feature.schedule.domain.usecase

import com.lovaltech.lovalplan.feature.schedule.domain.model.AgendaItem
import com.lovaltech.lovalplan.feature.schedule.domain.model.AgendaStatus
import com.lovaltech.lovalplan.feature.schedule.domain.model.ScheduleEvent
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * Construye la agenda de un dia: filtra las actividades que ocurren ese dia,
 * las ordena por hora y calcula su estado. Es una funcion pura (sin Room ni red),
 * por eso es facil de probar.
 *
 * @param completedKeys claves de ocurrencias cumplidas (ScheduleEvent.occurrenceKey).
 */
class BuildDayAgendaUseCase @Inject constructor() {
    operator fun invoke(
        day: LocalDate,
        now: LocalDateTime,
        events: List<ScheduleEvent>,
        completedKeys: Set<String>
    ): List<AgendaItem> =
        events
            .filter { it.occursOn(day) }
            .sortedBy { it.startMinute }
            .map { event ->
                val start = day.atTime(event.startTime)
                val end = day.atTime(event.endTime)
                val status = when {
                    ScheduleEvent.occurrenceKey(event.id, day) in completedKeys -> AgendaStatus.COMPLETED
                    now.isBefore(start) -> AgendaStatus.UPCOMING
                    now.isBefore(end) -> AgendaStatus.IN_PROGRESS
                    else -> AgendaStatus.MISSED
                }
                AgendaItem(event, day, status)
            }
}
