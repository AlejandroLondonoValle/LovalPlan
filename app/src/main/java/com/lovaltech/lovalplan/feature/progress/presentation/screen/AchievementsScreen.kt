package com.lovaltech.lovalplan.feature.progress.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lovaltech.lovalplan.core.designsystem.theme.Night
import com.lovaltech.lovalplan.feature.progress.presentation.composables.AchievementBadge
import com.lovaltech.lovalplan.feature.progress.presentation.section.LevelSection
import com.lovaltech.lovalplan.feature.progress.presentation.section.StreakWeekSection
import com.lovaltech.lovalplan.feature.progress.presentation.viewmodel.AchievementsViewModel

/** Pantalla "Logros": nivel, racha semanal y cuadricula de medallas. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(vm: AchievementsViewModel) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    Scaffold(
        containerColor = Night,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Logros") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Night)
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) { LevelSection(ui.stats) }
            item(span = { GridItemSpan(maxLineSpan) }) { StreakWeekSection(ui.stats) }
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    "Medallas · ${ui.unlockedCount} de ${ui.achievements.size}",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            items(ui.achievements, key = { it.id }) { achievement ->
                AchievementBadge(achievement)
            }
        }
    }
}
