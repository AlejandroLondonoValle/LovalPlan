package com.lovaltech.lovalplan.feature.profile.presentation.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lovaltech.lovalplan.R
import com.lovaltech.lovalplan.core.designsystem.theme.LovalBlueLight
import com.lovaltech.lovalplan.core.designsystem.theme.NightSurface
import com.lovaltech.lovalplan.core.designsystem.theme.StreakOrange
import com.lovaltech.lovalplan.core.designsystem.theme.TextSecondary
import com.lovaltech.lovalplan.core.designsystem.theme.XpGold
import com.lovaltech.lovalplan.feature.profile.presentation.composables.AvatarInitials
import com.lovaltech.lovalplan.feature.progress.domain.model.ProgressStats

/** Avatar, nombre editable, tipo de cuenta y 3 estadisticas. */
@Composable
fun ProfileHeaderSection(
    name: String,
    email: String?,
    isGuest: Boolean,
    stats: ProgressStats,
    onEditName: () -> Unit
) {
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AvatarInitials(name)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(name.ifBlank { "Sin nombre" }, style = MaterialTheme.typography.headlineSmall)
            IconButton(onClick = onEditName) {
                Icon(painterResource(R.drawable.ic_edit), contentDescription = "Editar nombre", tint = TextSecondary, modifier = Modifier.size(20.dp))
            }
        }
        Text(
            if (isGuest) "Invitado · sin cuenta vinculada" else "Cuenta de Google · ${email.orEmpty()}",
            color = TextSecondary,
            style = MaterialTheme.typography.bodyMedium
        )
        Row(Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ProfileStat("🏆", "Nivel ${stats.level}", "nivel", LovalBlueLight, Modifier.weight(1f))
            ProfileStat("⚡", "${stats.totalXp}", "XP total", XpGold, Modifier.weight(1f))
            ProfileStat("🔥", "${stats.bestStreak}", "mejor racha", StreakOrange, Modifier.weight(1f))
        }
    }
}

@Composable
private fun ProfileStat(emoji: String, value: String, label: String, color: Color, modifier: Modifier) {
    Column(
        modifier
            .background(NightSurface, RoundedCornerShape(18.dp))
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(emoji, style = MaterialTheme.typography.titleLarge)
        Text(value, style = MaterialTheme.typography.titleMedium, color = color)
        Text(label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
    }
}
