package com.lovaltech.lovalplan.feature.progress.domain.model

/**
 * Registro de que una actividad se cumplio un dia concreto.
 * Es un historial inmutable: si luego borras la actividad, tu XP y tu racha se conservan.
 *
 * @param id ScheduleEvent.occurrenceKey(eventId, dia) -> evita cumplir dos veces lo mismo.
 * @param startMinute copia de la hora de inicio (sirve para el logro "Madrugador").
 */
data class Completion(
    val id: String,
    val eventId: String,
    val dayEpoch: Long,
    val completedAt: Long,
    val xp: Int,
    val startMinute: Int
)
