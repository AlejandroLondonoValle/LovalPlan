package com.lovaltech.lovalplan.feature.progress

import com.lovaltech.lovalplan.fakes.TEST_CLOCK
import com.lovaltech.lovalplan.fakes.TODAY
import com.lovaltech.lovalplan.fakes.completion
import com.lovaltech.lovalplan.feature.progress.domain.model.XpRules
import com.lovaltech.lovalplan.feature.progress.domain.usecase.CalculateProgressUseCase
import org.junit.Assert.assertEquals
import org.junit.Test

class CalculateProgressUseCaseTest {

    private val calculate = CalculateProgressUseCase(TEST_CLOCK)

    @Test
    fun `sin historial la racha es cero y el nivel es 1`() {
        val stats = calculate(emptyList(), dailyGoal = 1, eventsCount = 0)
        assertEquals(0, stats.currentStreak)
        assertEquals(1, stats.level)
    }

    @Test
    fun `tres dias seguidos terminando hoy dan racha de 3`() {
        val history = listOf(completion(TODAY), completion(TODAY.minusDays(1)), completion(TODAY.minusDays(2)))
        assertEquals(3, calculate(history, 1, 1).currentStreak)
    }

    @Test
    fun `si hoy aun no cumple, la racha de ayer se conserva`() {
        val history = listOf(completion(TODAY.minusDays(1)), completion(TODAY.minusDays(2)))
        assertEquals(2, calculate(history, 1, 1).currentStreak)
    }

    @Test
    fun `un dia sin cumplir rompe la racha pero no la mejor racha`() {
        val history = listOf(
            completion(TODAY),
            // falta ayer
            completion(TODAY.minusDays(2)), completion(TODAY.minusDays(3)), completion(TODAY.minusDays(4))
        )
        val stats = calculate(history, 1, 1)
        assertEquals(1, stats.currentStreak)
        assertEquals(3, stats.bestStreak)
    }

    @Test
    fun `con meta diaria 2, un dia con 1 sola actividad no cuenta`() {
        val history = listOf(
            completion(TODAY, eventId = "a"), completion(TODAY, eventId = "b"),
            completion(TODAY.minusDays(1), eventId = "a")
        )
        assertEquals(1, calculate(history, dailyGoal = 2, eventsCount = 2).currentStreak)
    }

    @Test
    fun `cada 100 XP se sube de nivel`() {
        val history = (0 until 7).map { completion(TODAY.minusDays(it.toLong()), xp = 20) } // 140 XP
        val stats = calculate(history, 1, 1)
        assertEquals(140, stats.totalXp)
        assertEquals(2, stats.level)
        assertEquals(40, stats.xpIntoLevel)
    }

    @Test
    fun `la XP depende de la duracion con un maximo de 40`() {
        assertEquals(10, XpRules.xpFor(20))
        assertEquals(15, XpRules.xpFor(30))
        assertEquals(20, XpRules.xpFor(60))
        assertEquals(40, XpRules.xpFor(300))
    }
}
