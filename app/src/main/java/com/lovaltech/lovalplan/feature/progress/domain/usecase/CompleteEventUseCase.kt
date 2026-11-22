package com.lovaltech.lovalplan.feature.progress.domain.usecase

import com.lovaltech.lovalplan.core.util.TimeText
import com.lovaltech.lovalplan.feature.profile.domain.repository.ProfileRepository
import com.lovaltech.lovalplan.feature.progress.domain.model.Completion
import com.lovaltech.lovalplan.feature.progress.domain.model.CompletionResult
import com.lovaltech.lovalplan.feature.progress.domain.model.XpRules
import com.lovaltech.lovalplan.feature.progress.domain.repository.ProgressRepository
import com.lovaltech.lovalplan.feature.schedule.domain.model.ScheduleEvent
import com.lovaltech.lovalplan.feature.schedule.domain.repository.ScheduleRepository
import java.time.Clock
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * "¡Lo cumplí!". Reglas:
 *  1. La actividad debe existir y estar programada para HOY.
 *  2. Debe haber empezado (no se puede cumplir algo que aun no inicia).
 *  3. Solo se cumple una vez por dia.
 * Devuelve lo necesario para la celebracion: XP ganada, racha, nivel y logros nuevos.
 * Lo usan el Dashboard y el boton de la notificacion.
 */
class CompleteEventUseCase @Inject constructor(
    private val schedule: ScheduleRepository,
    private val progress: ProgressRepository,
    private val profile: ProfileRepository,
    private val calculate: CalculateProgressUseCase,
    private val evaluate: EvaluateAchievementsUseCase,
    private val clock: Clock
) {
    suspend operator fun invoke(eventId: String): CompletionResult {
        val now = LocalDateTime.now(clock)
        val today = now.toLocalDate()
        val event = schedule.findById(eventId)
            ?: throw IllegalArgumentException("La actividad ya no existe")
        require(event.occursOn(today)) { "Esta actividad no está programada para hoy" }
        require(!now.toLocalTime().isBefore(event.startTime)) {
            "Aún no empieza: podrás marcarla desde las ${TimeText.hhmm(event.startMinute)}"
        }
        val key = ScheduleEvent.occurrenceKey(eventId, today)
        require(progress.findCompletion(key) == null) { "¡Ya la cumpliste hoy!" }

        val dailyGoal = profile.getProfile().dailyGoal
        val eventsCount = schedule.getAll().size
        val before = calculate(progress.getCompletions(), dailyGoal, eventsCount)
        val unlockedBefore = evaluate(before).filter { it.unlocked }.map { it.id }.toSet()

        val xp = XpRules.xpFor(event.durationMinutes)
        progress.saveCompletion(
            Completion(
                id = key,
                eventId = eventId,
                dayEpoch = today.toEpochDay(),
                completedAt = clock.millis(),
                xp = xp,
                startMinute = event.startMinute
            )
        )

        val after = calculate(progress.getCompletions(), dailyGoal, eventsCount)
        return CompletionResult(
            eventTitle = event.title,
            xpGained = xp,
            currentStreak = after.currentStreak,
            level = after.level,
            leveledUp = after.level > before.level,
            dailyGoalJustMet = before.completedToday < dailyGoal && after.completedToday >= dailyGoal,
            newAchievements = evaluate(after).filter { it.unlocked && it.id !in unlockedBefore }
        )
    }
}
