package com.lovaltech.lovalplan.feature.auth.domain.usecase

import com.lovaltech.lovalplan.feature.auth.domain.repository.AuthRepository
import com.lovaltech.lovalplan.feature.profile.domain.repository.ProfileRepository
import com.lovaltech.lovalplan.feature.progress.domain.repository.ProgressRepository
import com.lovaltech.lovalplan.feature.schedule.domain.reminder.ReminderScheduler
import com.lovaltech.lovalplan.feature.schedule.domain.repository.ScheduleRepository
import javax.inject.Inject

/**
 * Cerrar sesion: cancela alarmas, limpia la base local y cierra Firebase Auth.
 * Asi el siguiente usuario no ve datos del anterior.
 */
class SignOutUseCase @Inject constructor(
    private val auth: AuthRepository,
    private val schedule: ScheduleRepository,
    private val progress: ProgressRepository,
    private val profile: ProfileRepository,
    private val scheduler: ReminderScheduler
) {
    suspend operator fun invoke() {
        schedule.getAll().forEach { scheduler.cancel(it.id) }
        schedule.clearLocal()
        progress.clearLocal()
        profile.clear()
        auth.signOut()
    }
}
