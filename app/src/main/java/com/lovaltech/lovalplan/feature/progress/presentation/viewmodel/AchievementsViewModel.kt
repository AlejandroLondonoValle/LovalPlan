package com.lovaltech.lovalplan.feature.progress.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovaltech.lovalplan.feature.profile.domain.repository.ProfileRepository
import com.lovaltech.lovalplan.feature.progress.domain.repository.ProgressRepository
import com.lovaltech.lovalplan.feature.progress.domain.usecase.CalculateProgressUseCase
import com.lovaltech.lovalplan.feature.progress.domain.usecase.EvaluateAchievementsUseCase
import com.lovaltech.lovalplan.feature.progress.presentation.state.AchievementsUiState
import com.lovaltech.lovalplan.feature.schedule.domain.repository.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/** Pantalla Logros: todo se recalcula solo cuando cambia el historial (Flow de Room). */
@HiltViewModel
class AchievementsViewModel @Inject constructor(
    progressRepository: ProgressRepository,
    profileRepository: ProfileRepository,
    scheduleRepository: ScheduleRepository,
    private val calculate: CalculateProgressUseCase,
    private val evaluate: EvaluateAchievementsUseCase
) : ViewModel() {

    val ui: StateFlow<AchievementsUiState> = combine(
        progressRepository.observeCompletions(),
        profileRepository.observeProfile(),
        scheduleRepository.observeEvents()
    ) { completions, profile, events ->
        val stats = calculate(completions, profile.dailyGoal, events.size)
        AchievementsUiState(stats = stats, achievements = evaluate(stats))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AchievementsUiState())
}
