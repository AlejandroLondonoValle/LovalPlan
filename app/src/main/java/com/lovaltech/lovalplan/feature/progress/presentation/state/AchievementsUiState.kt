package com.lovaltech.lovalplan.feature.progress.presentation.state

import com.lovaltech.lovalplan.feature.progress.domain.model.Achievement
import com.lovaltech.lovalplan.feature.progress.domain.model.ProgressStats

data class AchievementsUiState(
    val stats: ProgressStats = ProgressStats(),
    val achievements: List<Achievement> = emptyList()
) {
    val unlockedCount: Int get() = achievements.count { it.unlocked }
}
