package com.lovaltech.lovalplan.feature.auth.domain.model

/** Sesion actual. No expone FirebaseUser: el resto de la app no depende de Firebase. */
data class UserSession(
    val uid: String,
    val displayName: String?,
    val email: String?,
    val isGuest: Boolean
)
