package com.lovaltech.lovalplan.feature.progress.domain.usecase

import com.lovaltech.lovalplan.feature.progress.domain.model.Achievement
import com.lovaltech.lovalplan.feature.progress.domain.model.ProgressStats
import javax.inject.Inject

/**
 * Catalogo de logros. No se guardan en la base de datos: se CALCULAN a partir de las
 * estadisticas, asi nunca quedan desincronizados con el historial real.
 */
class EvaluateAchievementsUseCase @Inject constructor() {
    operator fun invoke(stats: ProgressStats): List<Achievement> = listOf(
        achievement("primer_paso", "Primer paso", "Cumple tu primera actividad", "🎯", stats.totalCompleted, 1),
        achievement("organizado", "Organizado", "Crea 5 actividades en tu horario", "🗓️", stats.eventsCount, 5),
        achievement("racha_3", "Calentando motores", "Racha de 3 días", "🔥", stats.bestStreak, 3),
        achievement("en_marcha", "En marcha", "Cumple 10 actividades", "🚀", stats.totalCompleted, 10),
        achievement("madrugador", "Madrugador", "Cumple una actividad que empiece antes de las 7:00", "🌅", stats.earlyBirdCount, 1),
        achievement("racha_7", "Semana de fuego", "Racha de 7 días", "🌟", stats.bestStreak, 7),
        achievement("nivel_5", "Nivel 5", "Alcanza el nivel 5", "🏆", stats.level, 5),
        achievement("imparable", "Imparable", "Cumple 50 actividades", "⚡", stats.totalCompleted, 50),
        achievement("racha_30", "Dragón disciplinado", "Racha de 30 días", "🐉", stats.bestStreak, 30),
        achievement("centenario", "Centenario", "Cumple 100 actividades", "💯", stats.totalCompleted, 100)
    )

    private fun achievement(id: String, title: String, description: String, emoji: String, value: Int, target: Int) =
        Achievement(id, title, description, emoji, progress = value.coerceAtMost(target), target = target)
}
