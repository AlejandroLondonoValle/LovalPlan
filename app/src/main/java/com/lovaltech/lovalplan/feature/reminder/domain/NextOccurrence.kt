package com.lovaltech.lovalplan.feature.reminder.domain

import com.lovaltech.lovalplan.feature.schedule.domain.model.RecurrenceType
import com.lovaltech.lovalplan.feature.schedule.domain.model.ScheduleEvent
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Calcula CUANDO debe sonar la proxima alarma de una actividad. Logica pura (sin Android),
 * por eso tiene pruebas unitarias.
 *
 * Solo se programa la PROXIMA ocurrencia; cuando suena, el receptor programa la siguiente.
 */
object NextOccurrence {

    /** Proximo aviso de inicio: (hora de inicio - minutos de anticipacion), posterior a [now]. */
    fun startTrigger(event: ScheduleEvent, now: LocalDateTime): LocalDateTime? =
        firstAfter(event, now) { day ->
            day.atTime(event.startTime).minusMinutes(event.reminderMinutes.toLong())
        }

    /** Proximo aviso de fin ("¿Lo cumpliste?"), posterior a [now]. */
    fun endTrigger(event: ScheduleEvent, now: LocalDateTime): LocalDateTime? =
        firstAfter(event, now) { day -> day.atTime(event.endTime) }

    private fun firstAfter(
        event: ScheduleEvent,
        now: LocalDateTime,
        triggerFor: (LocalDate) -> LocalDateTime
    ): LocalDateTime? {
        if (event.recurrence == RecurrenceType.ONCE) {
            val date = event.date ?: return null
            return triggerFor(date).takeIf { it.isAfter(now) }
        }
        // Semanal: basta revisar los proximos 8 dias para encontrar la siguiente ocurrencia.
        val today = now.toLocalDate()
        for (offset in 0..8) {
            val day = today.plusDays(offset.toLong())
            if (event.occursOn(day)) {
                val trigger = triggerFor(day)
                if (trigger.isAfter(now)) return trigger
            }
        }
        return null
    }
}
