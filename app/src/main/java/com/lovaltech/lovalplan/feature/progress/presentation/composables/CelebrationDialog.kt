package com.lovaltech.lovalplan.feature.progress.presentation.composables

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.lovaltech.lovalplan.R
import com.lovaltech.lovalplan.core.designsystem.components.DuoButton
import com.lovaltech.lovalplan.core.designsystem.components.StatChip
import com.lovaltech.lovalplan.core.designsystem.theme.LovalBlue
import com.lovaltech.lovalplan.core.designsystem.theme.LovalBlueLight
import com.lovaltech.lovalplan.core.designsystem.theme.NightSurface
import com.lovaltech.lovalplan.core.designsystem.theme.StreakOrange
import com.lovaltech.lovalplan.core.designsystem.theme.TextSecondary
import com.lovaltech.lovalplan.core.designsystem.theme.XpGold
import com.lovaltech.lovalplan.feature.progress.domain.model.CompletionResult

/** Celebracion estilo Duolingo al cumplir: el dragon "salta" y se muestran XP, racha y logros. */
@Composable
fun CelebrationDialog(result: CompletionResult, onDismiss: () -> Unit) {
    val bounce = remember { Animatable(0.3f) }
    LaunchedEffect(Unit) {
        bounce.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow))
    }
    Dialog(onDismissRequest = onDismiss) {
        Column(
            Modifier
                .fillMaxWidth()
                .background(NightSurface, RoundedCornerShape(28.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                Modifier
                    .size(120.dp)
                    .scale(bounce.value)
                    .background(LovalBlue, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(painterResource(R.drawable.ic_dragon), contentDescription = null, Modifier.size(70.dp))
            }
            Text(
                if (result.leveledUp) "¡Subiste a nivel ${result.level}!" else "¡Actividad cumplida!",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Text(result.eventTitle, color = TextSecondary, textAlign = TextAlign.Center)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatChip("⚡", "+${result.xpGained} XP", XpGold)
                StatChip("🔥", "${result.currentStreak}", StreakOrange)
            }
            if (result.dailyGoalJustMet) {
                Text("🎉 ¡Meta diaria cumplida! Tu racha está a salvo hoy.", color = LovalBlueLight, textAlign = TextAlign.Center)
            }
            result.newAchievements.forEach {
                Text("${it.emoji} Nuevo logro: ${it.title}", style = MaterialTheme.typography.titleMedium, color = XpGold)
            }
            DuoButton("Continuar", onClick = onDismiss, modifier = Modifier.fillMaxWidth())
        }
    }
}
