package com.lovaltech.lovalplan.feature.profile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovaltech.lovalplan.core.sync.SyncAllUseCase
import com.lovaltech.lovalplan.feature.auth.domain.repository.AuthRepository
import com.lovaltech.lovalplan.feature.auth.domain.usecase.SignOutUseCase
import com.lovaltech.lovalplan.feature.profile.domain.repository.ProfileRepository
import com.lovaltech.lovalplan.feature.profile.presentation.state.ProfileTransient
import com.lovaltech.lovalplan.feature.profile.presentation.state.ProfileUiState
import com.lovaltech.lovalplan.feature.progress.domain.repository.ProgressRepository
import com.lovaltech.lovalplan.feature.progress.domain.usecase.CalculateProgressUseCase
import com.lovaltech.lovalplan.feature.schedule.domain.repository.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    progressRepository: ProgressRepository,
    scheduleRepository: ScheduleRepository,
    private val auth: AuthRepository,
    private val calculate: CalculateProgressUseCase,
    private val syncAll: SyncAllUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val session = MutableStateFlow(auth.currentSession())
    private val transient = MutableStateFlow(ProfileTransient())

    val ui: StateFlow<ProfileUiState> = combine(
        profileRepository.observeProfile(),
        progressRepository.observeCompletions(),
        scheduleRepository.observeEvents(),
        session,
        transient
    ) { profile, completions, events, user, temp ->
        ProfileUiState(
            name = profile.displayName,
            email = user?.email,
            isGuest = user?.isGuest ?: true,
            dailyGoal = profile.dailyGoal,
            stats = calculate(completions, profile.dailyGoal, events.size),
            isSyncing = temp.isSyncing,
            message = temp.message,
            signedOut = temp.signedOut
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ProfileUiState())

    fun updateName(name: String) = viewModelScope.launch { profileRepository.updateName(name) }

    fun updateDailyGoal(goal: Int) = viewModelScope.launch { profileRepository.updateDailyGoal(goal) }

    fun sync() = viewModelScope.launch {
        transient.update { it.copy(isSyncing = true, message = null) }
        val message = try {
            val report = syncAll()
            "Sincronizado ✓ ${report.uploaded} enviados, ${report.downloaded} recibidos"
        } catch (e: Exception) {
            "Sin conexión: tus datos siguen guardados en el celular"
        }
        session.value = auth.currentSession()
        transient.update { it.copy(isSyncing = false, message = message) }
    }

    /** Invitado -> cuenta de Google conservando el mismo UID y sus datos. */
    fun linkGoogle(idToken: String) = viewModelScope.launch {
        transient.update { it.copy(isSyncing = true, message = null) }
        try {
            val user = auth.signInWithGoogle(idToken)
            if (profileRepository.getProfile().displayName.isBlank()) {
                user.displayName?.let { profileRepository.updateName(it) }
            }
            session.value = auth.currentSession()
            transient.update { it.copy(isSyncing = false, message = "Cuenta de Google vinculada ✓") }
        } catch (e: Exception) {
            transient.update { it.copy(isSyncing = false, message = "No se pudo vincular: ${e.message}") }
        }
    }

    fun showMessage(message: String) = transient.update { it.copy(message = message) }

    fun dismissMessage() = transient.update { it.copy(message = null) }

    fun signOut() = viewModelScope.launch {
        signOutUseCase()
        transient.update { it.copy(signedOut = true) }
    }
}
