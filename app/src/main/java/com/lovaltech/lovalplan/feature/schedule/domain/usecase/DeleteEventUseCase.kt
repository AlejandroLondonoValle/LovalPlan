package com.lovaltech.lovalplan.feature.schedule.domain.usecase

import com.lovaltech.lovalplan.feature.schedule.domain.reminder.ReminderScheduler
import com.lovaltech.lovalplan.feature.schedule.domain.repository.ScheduleRepository
import javax.inject.Inject

/** Borra (tombstone) la actividad y cancela sus alarmas pendientes. */
class DeleteEventUseCase @Inject constructor(
    private val repository: ScheduleRepository,
    private val scheduler: ReminderScheduler
) {
    suspend operator fun invoke(eventId: String) {
        repository.delete(eventId)
        scheduler.cancel(eventId)
    }
}
