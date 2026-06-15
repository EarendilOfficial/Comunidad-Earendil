package com.earendil.app.session

import android.content.Context
import androidx.credentials.CredentialManager
import android.util.Log
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

suspend fun signInWithGoogleAndFirebase(context: Context): String? {
    val credentialManager = CredentialManager.create(context)
    val firebaseAuth = FirebaseAuth.getInstance()

    // 1. Configurar la petición usando tu Web Client ID de Firebase
    val googleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false) // Ponlo en false si quieres forzar a que elija cuenta la primera vez
        .setServerClientId("1076977723843-tgvsoid6uh0ac7e0ptnqqfvalvv7ou8h.apps.googleusercontent.com")
        .build()

    val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    try {
        // 2. Lanzar la interfaz de Credential Manager
        val result = credentialManager.getCredential(context = context, request = request)
        val credential = result.credential

        // 3. Validar si la credencial es un Token de Identidad de Google
        if (credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {

            // Extraer de forma segura el ID Token de Google
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val idToken = googleIdTokenCredential.idToken

            // 4. GENERAR LA CREDENCIAL DE FIREBASE Y HACER EL LOGIN
            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)

            // Usamos .await() de kotlinx-coroutines-play-services para mantenerlo asíncrono
            val authResult = firebaseAuth.signInWithCredential(firebaseCredential).await()
            val firebaseUser = authResult.user

            Log.d("AUTH", "¡Login exitoso en Firebase! Bienvenido: ${firebaseUser?.displayName}")
            val firebaseIdToken = firebaseUser?.getIdToken(true)?.await()?.token;

            return firebaseIdToken;
        } else {
            Log.e("AUTH", "Tipo de credencial no esperado.")
            return null
        }

    } catch (e: GetCredentialException) {
        Log.e("AUTH", "Error del Credential Manager: ${e.message}")
    } catch (e: GoogleIdTokenParsingException) {
        Log.e("AUTH", "Error al parsear el token de Google: ${e.message}")
    } catch (e: Exception) {
        Log.e("AUTH", "Error al autenticar en Firebase: ${e.message}")
    }

    // in case of exception
    return null;
}