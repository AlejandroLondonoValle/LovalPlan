package com.lovaltech.lovalplan.feature.dashboard.presentation.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lovaltech.lovalplan.R
import com.lovaltech.lovalplan.core.designsystem.components.StatChip
import com.lovaltech.lovalplan.core.designsystem.theme.LovalBlue
import com.lovaltech.lovalplan.core.designsystem.theme.LovalBlueLight
import com.lovaltech.lovalplan.core.designsystem.theme.StreakOrange
import com.lovaltech.lovalplan.core.designsystem.theme.TextSecondary
import com.lovaltech.lovalplan.core.designsystem.theme.XpGold
import com.lovaltech.lovalplan.feature.progress.domain.model.ProgressStats

/** Saludo + racha, XP y nivel + boton de sincronizar. */
@Composable
fun DashboardHeaderSection(
    greeting: String,
    name: String,
    stats: ProgressStats,
    isSyncing: Boolean,
    onSync: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("$greeting, $name!", style = MaterialTheme.typography.headlineSmall)
                Text("Vamos por tus metas de hoy", color = TextSecondary)
            }
            if (isSyncing) {
                CircularProgressIndicator(color = LovalBlue, strokeWidth = 3.dp, modifier = Modifier.padding(10.dp).size(28.dp))
            } else {
                IconButton(onClick = onSync) {
                    Icon(painterResource(R.drawable.ic_sync), contentDescription = "Sincronizar", tint = LovalBlueLight)
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatChip("🔥", "${stats.currentStreak}", StreakOrange)
            StatChip("⚡", "${stats.totalXp} XP", XpGold)
            StatChip("🏆", "Nv ${stats.level}", LovalBlueLight)
        }
    }
}
