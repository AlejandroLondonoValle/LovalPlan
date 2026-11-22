package com.lovaltech.lovalplan.feature.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovaltech.lovalplan.feature.auth.domain.repository.AuthRepository
import com.lovaltech.lovalplan.feature.profile.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SessionState {
    data object Loading : SessionState
    data object NeedsLogin : SessionState
    data object Ready : SessionState
}

/** Decide UNA vez al abrir la app si se muestra el Login o el Dashboard. */
@HiltViewModel
class SessionViewModel @Inject constructor(
    private val auth: AuthRepository,
    private val profile: ProfileRepository
) : ViewModel() {
    private val _state = MutableStateFlow<SessionState>(SessionState.Loading)
    val state: StateFlow<SessionState> = _state

    init {
        viewModelScope.launch {
            val onboarded = profile.getProfile().onboarded
            _state.value = if (onboarded || auth.currentSession() != null) {
                SessionState.Ready
            } else {
                SessionState.NeedsLogin
            }
        }
    }
}
