package com.lovaltech.lovalplan.feature.progress.domain.usecase

import com.lovaltech.lovalplan.feature.progress.domain.model.Completion
import com.lovaltech.lovalplan.feature.progress.domain.model.DayProgress
import com.lovaltech.lovalplan.feature.progress.domain.model.ProgressStats
import com.lovaltech.lovalplan.feature.progress.domain.model.XpRules
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

/**
 * Calcula XP, nivel y rachas a partir del historial de cumplimientos.
 *
 * Regla de la racha (como Duolingo):
 *  - Un dia "cuenta" si cumpliste al menos [dailyGoal] actividades.
 *  - La racha actual son los dias seguidos que cuentan, terminando HOY o AYER.
 *    (Si hoy aun no llegas a la meta, no pierdes la racha: tienes hasta medianoche.)
 */
class CalculateProgressUseCase @Inject constructor(
    private val clock: Clock
) {
    operator fun invoke(
        completions: List<Completion>,
        dailyGoal: Int,
        eventsCount: Int
    ): ProgressStats {
        val goal = dailyGoal.coerceAtLeast(1)
        val today = LocalDate.now(clock)
        val todayEpoch = today.toEpochDay()
        val perDay: Map<Long, Int> = completions.groupingBy { it.dayEpoch }.eachCount()
        fun met(day: Long) = (perDay[day] ?: 0) >= goal

        // Racha actual: desde hoy (si ya cumplio) o desde ayer, hacia atras.
        var cursor = if (met(todayEpoch)) todayEpoch else todayEpoch - 1
        var current = 0
        while (met(cursor)) {
            current++
            cursor--
        }

        // Mejor racha historica: la secuencia mas larga de dias consecutivos que cuentan.
        var best = 0
        var run = 0
        var previous: Long? = null
        for (day in perDay.filterValues { it >= goal }.keys.sorted()) {
            run = if (previous != null && day == previous + 1) run + 1 else 1
            best = maxOf(best, run)
            previous = day
        }

        val totalXp = completions.sumOf { it.xp }
        val lastSeven = (6 downTo 0).map { back ->
            val date = today.minusDays(back.toLong())
            val count = perDay[date.toEpochDay()] ?: 0
            DayProgress(date, count, count >= goal)
        }

        return ProgressStats(
            totalXp = totalXp,
            level = XpRules.levelFor(totalXp),
            xpIntoLevel = XpRules.xpIntoLevel(totalXp),
            xpPerLevel = XpRules.XP_PER_LEVEL,
            totalCompleted = completions.size,
            completedToday = perDay[todayEpoch] ?: 0,
            dailyGoal = goal,
            currentStreak = current,
            bestStreak = maxOf(best, current),
            earlyBirdCount = completions.count { it.startMinute < XpRules.EARLY_BIRD_LIMIT_MINUTE },
            eventsCount = eventsCount,
            lastSevenDays = lastSeven
        )
    }
}
