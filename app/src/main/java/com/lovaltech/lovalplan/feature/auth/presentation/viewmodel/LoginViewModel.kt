package com.lovaltech.lovalplan.feature.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovaltech.lovalplan.feature.auth.domain.repository.AuthRepository
import com.lovaltech.lovalplan.feature.auth.presentation.state.LoginUiState
import com.lovaltech.lovalplan.feature.profile.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: AuthRepository,
    private val profile: ProfileRepository
) : ViewModel() {
    private val _ui = MutableStateFlow(LoginUiState())
    val ui: StateFlow<LoginUiState> = _ui

    fun onGoogleStarted() {
        _ui.value = _ui.value.copy(loading = true, error = null)
    }

    /** La pantalla consiguio el ID token de Google; aqui se lo damos a Firebase. */
    fun onGoogleToken(idToken: String) = viewModelScope.launch {
        _ui.value = _ui.value.copy(loading = true, error = null)
        try {
            val session = auth.signInWithGoogle(idToken)
            profile.completeOnboarding(session.displayName)
            _ui.value = _ui.value.copy(loading = false, done = true)
        } catch (e: Exception) {
            _ui.value = _ui.value.copy(loading = false, error = "No se pudo entrar con Google: ${e.message}")
        }
    }

    fun onGoogleError(message: String) {
        _ui.value = _ui.value.copy(loading = false, error = message)
    }

    /** Invitado: intenta la sesion anonima; si no hay red, entra igual (se crea al sincronizar). */
    fun continueAsGuest() = viewModelScope.launch {
        _ui.value = _ui.value.copy(loading = true, error = null)
        auth.signInAsGuest()
        profile.completeOnboarding(null)
        _ui.value = _ui.value.copy(loading = false, done = true)
    }
}
