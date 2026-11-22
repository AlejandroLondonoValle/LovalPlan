package com.lovaltech.lovalplan.feature.progress.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lovaltech.lovalplan.core.designsystem.components.XpBar
import com.lovaltech.lovalplan.core.designsystem.theme.LovalBlue
import com.lovaltech.lovalplan.core.designsystem.theme.LovalBlueLight
import com.lovaltech.lovalplan.core.designsystem.theme.NightOutline
import com.lovaltech.lovalplan.core.designsystem.theme.NightSurface
import com.lovaltech.lovalplan.core.designsystem.theme.TextSecondary
import com.lovaltech.lovalplan.feature.progress.domain.model.Achievement

/** Medalla de un logro: brillante si esta desbloqueado, apagada con progreso si no. */
@Composable
fun AchievementBadge(achievement: Achievement, modifier: Modifier = Modifier) {
    val unlocked = achievement.unlocked
    Column(
        modifier
            .background(NightSurface, RoundedCornerShape(20.dp))
            .border(2.dp, if (unlocked) LovalBlue else NightOutline, RoundedCornerShape(20.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            Modifier
                .size(64.dp)
                .background(if (unlocked) LovalBlue else NightOutline, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                achievement.emoji,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.alpha(if (unlocked) 1f else 0.35f)
            )
        }
        Text(
            achievement.title,
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            color = if (unlocked) LovalBlueLight else TextSecondary
        )
        Text(
            achievement.description,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = TextSecondary,
            minLines = 2
        )
        if (!unlocked) {
            XpBar(
                progress = achievement.progress.toFloat() / achievement.target,
                color = LovalBlue,
                modifier = Modifier.fillMaxWidth()
            )
            Text("${achievement.progress}/${achievement.target}", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        } else {
            Text("¡Desbloqueado!", style = MaterialTheme.typography.labelMedium, color = LovalBlueLight)
        }
    }
}
