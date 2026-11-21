package com.lovaltech.lovalplan.feature.auth.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.lovaltech.lovalplan.feature.auth.domain.model.UserSession
import com.lovaltech.lovalplan.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/** Unica clase que conoce FirebaseAuth. */
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    override fun currentSession(): UserSession? = auth.currentUser?.toSession()

    override suspend fun requireUid(): String =
        auth.currentUser?.uid
            ?: auth.signInAnonymously().await().user?.uid
            ?: error("No fue posible iniciar sesión anónima")

    override suspend fun signInWithGoogle(idToken: String): UserSession {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val current = auth.currentUser
        val result = if (current != null && current.isAnonymous) {
            try {
                // El invitado se convierte en cuenta de Google SIN cambiar de UID:
                // sus datos en Firestore siguen siendo suyos.
                current.linkWithCredential(credential).await()
            } catch (e: FirebaseAuthUserCollisionException) {
                // Esa cuenta de Google ya existia: simplemente se inicia sesion con ella.
                auth.signInWithCredential(credential).await()
            }
        } else {
            auth.signInWithCredential(credential).await()
        }
        return result.user?.toSession() ?: error("No fue posible iniciar sesión con Google")
    }

    override suspend fun signInAsGuest(): UserSession? =
        auth.currentUser?.toSession()
            ?: runCatching { auth.signInAnonymously().await().user?.toSession() }.getOrNull()

    override fun signOut() = auth.signOut()
}

private fun FirebaseUser.toSession() = UserSession(
    uid = uid,
    displayName = displayName?.takeIf { it.isNotBlank() }
        ?: providerData.firstNotNullOfOrNull { it.displayName?.takeIf(String::isNotBlank) },
    email = email ?: providerData.firstNotNullOfOrNull { it.email },
    isGuest = isAnonymous
)
