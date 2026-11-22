package com.lovaltech.lovalplan.feature.progress

import com.lovaltech.lovalplan.feature.progress.domain.model.ProgressStats
import com.lovaltech.lovalplan.feature.progress.domain.usecase.EvaluateAchievementsUseCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EvaluateAchievementsUseCaseTest {

    private val evaluate = EvaluateAchievementsUseCase()

    @Test
    fun `al empezar no hay logros desbloqueados`() {
        assertTrue(evaluate(ProgressStats()).none { it.unlocked })
    }

    @Test
    fun `primera actividad y racha de 3 desbloquean sus logros`() {
        val achievements = evaluate(ProgressStats(totalCompleted = 1, bestStreak = 3)).associateBy { it.id }
        assertTrue(achievements.getValue("primer_paso").unlocked)
        assertTrue(achievements.getValue("racha_3").unlocked)
        assertFalse(achievements.getValue("racha_7").unlocked)
        assertEquals(3, achievements.getValue("racha_7").progress)
    }
}
