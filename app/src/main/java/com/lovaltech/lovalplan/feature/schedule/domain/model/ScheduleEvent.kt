package com.lovaltech.lovalplan.feature.schedule.domain.model

import java.time.LocalDate
import java.time.LocalTime

/** Tipo de actividad. Los textos, emojis y colores viven en la capa de presentacion. */
enum class EventCategory { STUDY, WORK, EXERCISE, PERSONAL, HOME, OTHER }

/** WEEKLY se repite en [ScheduleEvent.daysOfWeek]; ONCE ocurre solo en [ScheduleEvent.date]. */
enum class RecurrenceType { WEEKLY, ONCE }

/**
 * Modelo de dominio de una actividad del horario. Equivale a Product en la fase 1:
 * solo tipos de Kotlin/Java, sin anotaciones de Room ni de Firebase.
 *
 * Las horas se guardan como "minuto del dia" (0..1439): 6:30 = 390.
 */
data class ScheduleEvent(
    val id: String,
    val title: String,
    val description: String,
    val category: EventCategory,
    val recurrence: RecurrenceType,
    val daysOfWeek: Set<Int>,          // ISO: 1 = lunes ... 7 = domingo
    val date: LocalDate?,              // solo para ONCE
    val startMinute: Int,
    val endMinute: Int,
    val reminderMinutes: Int,          // minutos de anticipacion de la alarma
    val updatedAt: Long = System.currentTimeMillis()
) {
    val durationMinutes: Int get() = endMinute - startMinute

    val startTime: LocalTime get() = LocalTime.ofSecondOfDay(startMinute * 60L)
    val endTime: LocalTime get() = LocalTime.ofSecondOfDay(endMinute * 60L)

    /** Regla central del horario: ¿esta actividad ocurre en ese dia? */
    fun occursOn(day: LocalDate): Boolean = when (recurrence) {
        RecurrenceType.WEEKLY -> day.dayOfWeek.value in daysOfWeek
        RecurrenceType.ONCE -> date == day
    }

    companion object {
        /**
         * Identificador de UNA ocurrencia (actividad + dia). Se usa como id del
         * registro de cumplimiento: "gym_20420" = gimnasio del dia 20420.
         */
        fun occurrenceKey(eventId: String, day: LocalDate): String = "${eventId}_${day.toEpochDay()}"
    }
}
