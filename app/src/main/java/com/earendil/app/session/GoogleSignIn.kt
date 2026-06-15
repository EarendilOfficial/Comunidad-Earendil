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
        .setServerClientId("1076977723843-tgvsoid6uh0ac7e0ptnqqfvalvv7ou8h.apps.googleusercontent.com")
        .setFilterByAuthorizedAccounts(false) // Ponlo en false si quieres forzar a que elija cuenta la primera vez
        .setAutoSelectEnabled(false)
        .build()

    val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    try {
        // 2. Lanzar la interfaz de Credential Manager
        Log.d("AUTH", "Iniciando Credential Manager...")
        val result = credentialManager.getCredential(context = context, request = request)
        Log.d("AUTH", "Credential Manager retornó resultado")
        val credential = result.credential

        // 3. Validar si la credencial es un Token de Identidad de Google
        if (credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {

            // Extraer de forma segura el ID Token de Google
            Log.d("AUTH", "Iniciando GoogleIdTokenCredential...")
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val idToken = googleIdTokenCredential.idToken
            Log.d("AUTH", "GoogleIdTokenCredential retornó token")


            // 4. GENERAR LA CREDENCIAL DE FIREBASE Y HACER EL LOGIN
            Log.d("AUTH", "GoogleAuthProvider getCredential ?...")
            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
            Log.d("AUTH", "GoogleAuthProvider")

            // Usamos .await() de kotlinx-coroutines-play-services para mantenerlo asíncrono
            Log.d("AUTH", "Esperando firebaseAuth.signInWithCredential(firebaseCredential).await()")
            val authResult = firebaseAuth.signInWithCredential(firebaseCredential).await()
            Log.d("AUTH", "signInWithCredential retornó resultado")

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