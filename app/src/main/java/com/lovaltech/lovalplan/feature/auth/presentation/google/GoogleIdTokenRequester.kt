package com.lovaltech.lovalplan.feature.auth.presentation.google

import android.annotation.SuppressLint
import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

sealed interface GoogleTokenResult {
    data class Success(val idToken: String) : GoogleTokenResult
    data class Failure(val message: String) : GoogleTokenResult
}

/**
 * Abre el selector de cuentas de Google (Credential Manager) y devuelve el ID token.
 * Necesita el contexto de la Activity porque muestra una hoja inferior del sistema;
 * por eso lo llama la Screen y luego le entrega el token al ViewModel.
 */
object GoogleIdTokenRequester {

    @SuppressLint("DiscouragedApi")
    suspend fun request(context: Context): GoogleTokenResult {
        // default_web_client_id lo genera el plugin google-services SOLO si el JSON trae
        // el cliente OAuth (Google activado + SHA-1). Se busca en tiempo de ejecucion
        // para que el proyecto compile aunque todavia no este configurado.
        val resId = context.resources.getIdentifier("default_web_client_id", "string", context.packageName)
        if (resId == 0) {
            return GoogleTokenResult.Failure(
                "Google aún no está configurado: activa el proveedor Google en Firebase, " +
                    "agrega tu SHA-1 y descarga de nuevo google-services.json. " +
                    "Mientras tanto puedes entrar como invitado."
            )
        }
        val option = GetSignInWithGoogleOption.Builder(context.getString(resId)).build()
        val request = GetCredentialRequest.Builder().addCredentialOption(option).build()
        return try {
            val credential = CredentialManager.create(context).getCredential(context, request).credential
            if (credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                GoogleTokenResult.Success(GoogleIdTokenCredential.createFrom(credential.data).idToken)
            } else {
                GoogleTokenResult.Failure("Se recibió un tipo de credencial inesperado")
            }
        } catch (e: GetCredentialCancellationException) {
            GoogleTokenResult.Failure("Inicio de sesión cancelado")
        } catch (e: GetCredentialException) {
            GoogleTokenResult.Failure(e.message ?: "No se pudo obtener la cuenta de Google")
        }
    }

    /** Al cerrar sesion, olvida la cuenta elegida para que vuelva a preguntar. */
    suspend fun clear(context: Context) {
        runCatching { CredentialManager.create(context).clearCredentialState(ClearCredentialStateRequest()) }
    }
}
