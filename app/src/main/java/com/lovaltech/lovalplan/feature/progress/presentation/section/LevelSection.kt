package com.lovaltech.lovalplan.feature.progress.presentation.section

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lovaltech.lovalplan.R
import com.lovaltech.lovalplan.core.designsystem.components.XpBar
import com.lovaltech.lovalplan.core.designsystem.theme.LovalBlue
import com.lovaltech.lovalplan.core.designsystem.theme.LovalBlueDark
import com.lovaltech.lovalplan.core.designsystem.theme.TextPrimary
import com.lovaltech.lovalplan.feature.progress.domain.model.ProgressStats

/** Tarjeta de nivel: dragon, nivel actual y barra de XP hacia el siguiente. */
@Composable
fun LevelSection(stats: ProgressStats) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Brush.horizontalGradient(listOf(LovalBlue, LovalBlueDark)), RoundedCornerShape(24.dp))
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(Modifier.size(72.dp), contentAlignment = Alignment.Center) {
            Image(painterResource(R.drawable.ic_dragon), contentDescription = null, Modifier.size(64.dp))
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Nivel ${stats.level}", style = MaterialTheme.typography.headlineMedium, color = TextPrimary)
            XpBar(progress = stats.xpIntoLevel.toFloat() / stats.xpPerLevel, modifier = Modifier.fillMaxWidth())
            Text(
                "${stats.xpIntoLevel} / ${stats.xpPerLevel} XP · Total ${stats.totalXp} XP",
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary
            )
        }
    }
}
