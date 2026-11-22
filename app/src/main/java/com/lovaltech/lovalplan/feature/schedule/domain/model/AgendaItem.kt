package com.lovaltech.lovalplan.feature.schedule.domain.model

import java.time.LocalDate

/** Estado de una actividad en un dia concreto. */
enum class AgendaStatus {
    UPCOMING,     // todavia no empieza
    IN_PROGRESS,  // esta ocurriendo ahora
    MISSED,       // ya termino y no se marco como cumplida
    COMPLETED     // cumplida
}

/** Una actividad "puesta" en un dia: lo que se pinta en el Dashboard y en el Horario. */
data class AgendaItem(
    val event: ScheduleEvent,
    val date: LocalDate,
    val status: AgendaStatus
)
