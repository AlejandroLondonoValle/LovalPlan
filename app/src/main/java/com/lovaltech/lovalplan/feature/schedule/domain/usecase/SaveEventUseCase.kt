package com.lovaltech.lovalplan.feature.schedule.domain.usecase

import com.lovaltech.lovalplan.feature.schedule.domain.model.RecurrenceType
import com.lovaltech.lovalplan.feature.schedule.domain.model.ScheduleEvent
import com.lovaltech.lovalplan.feature.schedule.domain.reminder.ReminderScheduler
import com.lovaltech.lovalplan.feature.schedule.domain.repository.ScheduleRepository
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

/**
 * Equivale a SaveProductUseCase: concentra TODAS las reglas para guardar una actividad.
 * Despues de guardar en Room, programa su alarma.
 */
class SaveEventUseCase @Inject constructor(
    private val repository: ScheduleRepository,
    private val scheduler: ReminderScheduler,
    private val clock: Clock
) {
    suspend operator fun invoke(event: ScheduleEvent) {
        require(event.title.isNotBlank()) { "El título es obligatorio" }
        require(event.title.length <= 60) { "Título: máximo 60 caracteres" }
        require(event.description.length <= 300) { "Descripción: máximo 300 caracteres" }
        require(event.startMinute in 0..1438 && event.endMinute in 1..1439) { "Hora inválida" }
        require(event.endMinute > event.startMinute) { "La hora de fin debe ser posterior al inicio" }
        require(event.durationMinutes >= 5) { "La actividad debe durar al menos 5 minutos" }
        require(event.reminderMinutes in 0..120) { "El aviso debe ser de 0 a 120 minutos antes" }
        when (event.recurrence) {
            RecurrenceType.WEEKLY -> require(
                event.daysOfWeek.isNotEmpty() && event.daysOfWeek.all { it in 1..7 }
            ) { "Elige al menos un día de la semana" }
            RecurrenceType.ONCE -> {
                val date = requireNotNull(event.date) { "Elige una fecha" }
                require(!date.isBefore(LocalDate.now(clock))) { "La fecha no puede estar en el pasado" }
            }
        }
        repository.save(event)
        scheduler.schedule(event)
    }
}
