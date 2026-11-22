package com.lovaltech.lovalplan.feature.profile.presentation.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lovaltech.lovalplan.core.designsystem.theme.NightSurface
import com.lovaltech.lovalplan.core.designsystem.theme.StreakOrange
import com.lovaltech.lovalplan.core.designsystem.theme.TextSecondary
import com.lovaltech.lovalplan.feature.schedule.presentation.composables.SelectableChip

/** Meta diaria: cuantas actividades cumplir al dia para mantener la racha. */
@Composable
fun DailyGoalSection(goal: Int, onSelect: (Int) -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(NightSurface, RoundedCornerShape(24.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("🎯 Meta diaria", style = MaterialTheme.typography.titleMedium)
        Text(
            "Actividades que debes cumplir cada día para que tu racha 🔥 siga creciendo.",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            (1..5).forEach { value ->
                SelectableChip(
                    text = "$value",
                    selected = goal == value,
                    onClick = { onSelect(value) },
                    selectedColor = StreakOrange,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
