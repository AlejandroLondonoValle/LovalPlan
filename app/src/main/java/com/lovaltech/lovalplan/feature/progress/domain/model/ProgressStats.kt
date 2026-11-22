package com.lovaltech.lovalplan.feature.progress.domain.model

import java.time.LocalDate

/** Cuantas actividades se cumplieron un dia y si se alcanzo la meta diaria. */
data class DayProgress(val date: LocalDate, val completed: Int, val goalMet: Boolean)

/** Todo lo que la gamificacion necesita saber, calculado a partir del historial. */
data class ProgressStats(
    val totalXp: Int = 0,
    val level: Int = 1,
    val xpIntoLevel: Int = 0,
    val xpPerLevel: Int = 100,
    val totalCompleted: Int = 0,
    val completedToday: Int = 0,
    val dailyGoal: Int = 1,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val earlyBirdCount: Int = 0,
    val eventsCount: Int = 0,
    val lastSevenDays: List<DayProgress> = emptyList()
)
