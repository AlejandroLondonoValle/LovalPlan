package com.lovaltech.lovalplan.feature.auth.presentation.state

data class LoginUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val done: Boolean = false
)
