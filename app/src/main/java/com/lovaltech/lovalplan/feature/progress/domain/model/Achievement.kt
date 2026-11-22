package com.lovaltech.lovalplan.feature.progress.domain.model

/** Un logro: se desbloquea cuando progress llega a target. */
data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    val progress: Int,
    val target: Int
) {
    val unlocked: Boolean get() = progress >= target
}

/** Lo que devuelve "¡Lo cumplí!": se usa para la celebracion. */
data class CompletionResult(
    val eventTitle: String,
    val xpGained: Int,
    val currentStreak: Int,
    val level: Int,
    val leveledUp: Boolean,
    val dailyGoalJustMet: Boolean,
    val newAchievements: List<Achievement>
)
