package com.lovaltech.lovalplan.feature.auth.domain.repository

import com.lovaltech.lovalplan.feature.auth.domain.model.UserSession

/** Contrato de autenticacion: Google o invitado (anonimo). */
interface AuthRepository {
    fun currentSession(): UserSession?

    /** UID para las rutas de Firestore. Si no hay sesion, inicia una anonima (necesita red). */
    suspend fun requireUid(): String

    /** Inicia sesion con el ID token de Google. Si eras invitado, vincula la cuenta (conserva el UID). */
    suspend fun signInWithGoogle(idToken: String): UserSession

    /** Intenta crear la sesion anonima. Devuelve null si no hay Internet (se reintenta al sincronizar). */
    suspend fun signInAsGuest(): UserSession?

    fun signOut()
}
