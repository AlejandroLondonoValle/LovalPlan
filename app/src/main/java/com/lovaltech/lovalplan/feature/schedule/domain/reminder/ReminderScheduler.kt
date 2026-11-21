package com.lovaltech.lovalplan.feature.schedule.domain.reminder

import com.lovaltech.lovalplan.feature.schedule.domain.model.ScheduleEvent

/**
 * Contrato para programar las alarmas de una actividad.
 * El dominio no sabe que existe AlarmManager: la implementacion real esta en
 * feature/reminder/data/AlarmReminderScheduler y Hilt la enlaza (RepositoryModule).
 * Gracias a esto, en las pruebas usamos un scheduler falso.
 */
interface ReminderScheduler {
    fun schedule(event: ScheduleEvent)
    fun cancel(eventId: String)
    fun rescheduleAll(events: List<ScheduleEvent>)
}
