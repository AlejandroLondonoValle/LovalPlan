package com.lovaltech.lovalplan.feature.progress.domain.model

/**
 * Reglas de experiencia en un solo lugar (faciles de explicar y de probar):
 * - Cada actividad cumplida da 10 XP + 5 XP por cada media hora completa, maximo 40.
 * - Cada nivel requiere 100 XP.
 */
object XpRules {
    const val XP_PER_LEVEL = 100
    const val EARLY_BIRD_LIMIT_MINUTE = 7 * 60   // actividades que empiezan antes de las 7:00

    fun xpFor(durationMinutes: Int): Int = (10 + (durationMinutes / 30) * 5).coerceAtMost(40)

    fun levelFor(totalXp: Int): Int = 1 + totalXp / XP_PER_LEVEL

    fun xpIntoLevel(totalXp: Int): Int = totalXp % XP_PER_LEVEL
}
