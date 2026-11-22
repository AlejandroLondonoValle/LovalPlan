package com.lovaltech.lovalplan.core.sync

import com.lovaltech.lovalplan.feature.progress.domain.repository.ProgressRepository
import com.lovaltech.lovalplan.feature.schedule.domain.reminder.ReminderScheduler
import com.lovaltech.lovalplan.feature.schedule.domain.repository.ScheduleRepository
import javax.inject.Inject

/**
 * Sincroniza las dos colecciones (horario y cumplimientos) y, como pudieron
 * llegar actividades nuevas desde la nube, reprograma todas las alarmas.
 */
class SyncAllUseCase @Inject constructor(
    private val schedule: ScheduleRepository,
    private val progress: ProgressRepository,
    private val scheduler: ReminderScheduler
) {
    suspend operator fun invoke(): SyncReport {
        val events = schedule.syncNow()
        val completions = progress.syncNow()
        scheduler.rescheduleAll(schedule.getAll())
        return SyncReport(
            uploaded = events.uploaded + completions.uploaded,
            downloaded = events.downloaded + completions.downloaded
        )
    }
}
