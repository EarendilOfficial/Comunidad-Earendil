package com.earendil.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.earendil.app.rest.ApiService
import kotlinx.coroutines.launch
import com.earendil.app.rest.GoogleLoginRequest
import com.earendil.app.rest.RetrofitClient
import com.earendil.app.session.SessionManager
import com.earendil.app.session.signInWithGoogleAndFirebase
import kotlin.math.log

class LoginActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 1. Inicializar el manejador de sesión y el cliente de red
        sessionManager = SessionManager(this)
        apiService = RetrofitClient.create { sessionManager.fetchAuthToken() }

        // 2. Implementar Login Persistente
        // La fuente de la verdad ahora es el token de tu backend Node.js, no Firebase.
        if (sessionManager.fetchAuthToken() != null) {
            Log.d("AUTH", "Sesión detectada, redirigiendo a MainActivity...")
            abrirMain()
            return
        }

        // 3. Configurar el botón
        findViewById<Button>(R.id.btnGoogle).setOnClickListener {
            // Deshabilitar el botón temporalmente para evitar doble clic
            it.isEnabled = false
            iniciarSesionGoogle(it as Button)
        }

//        findViewById<Button>(R.id.btnRegistrar).setOnClickListener {
//            startActivity(Intent(this, RegisterActivity::class.java))
//        }
//
//        find
    }

    private fun iniciarSesionGoogle(btn: Button) {
        // Como las llamadas de red y CredentialManager usan "suspend",
        // necesitamos lanzarlas dentro del ciclo de vida de esta Activity
        lifecycleScope.launch {
            try {
                // PASO A: Obtener el token de Firebase desde el dispositivo
                // (Llama a la función suspendida que creamos anteriormente)
                val firebaseToken = signInWithGoogleAndFirebase(this@LoginActivity)

                if (firebaseToken != null) {
                    // PASO B: Enviar ese token a tu servidor Node.js mediante Retrofit
                    val requestBody = GoogleLoginRequest(idToken = firebaseToken)
                    val response = apiService.loginWithGoogle(requestBody)

                    Log.d("LOL","${response.isSuccessful} + ${response.body()?.successful}");
                    // PASO C: Procesar la respuesta de tu servidor
                    if (response.isSuccessful && response.body()?.successful == true) {

                        // ¡Extraer y guardar el JWT de Node.js!
                        val nodeJsToken = response.body()?.token
                        if (nodeJsToken != null) {
                            sessionManager.saveAuthToken(nodeJsToken)
                            Log.d("AUTH", "¡JWT de Node.js guardado exitosamente!")
                            abrirMain()
                        } else {
                            Toast.makeText(this@LoginActivity, "Error: El servidor no devolvió el token", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        // El servidor rechazó el inicio de sesión
                        val errorReason = response.body()?.reason ?: "Error desconocido"
                        Toast.makeText(this@LoginActivity, "Rechazado: $errorReason", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("LoginActivity", "Excepción en el flujo híbrido: ${e.message}")
                Toast.makeText(this@LoginActivity, "Hubo un problema al conectar con el servidor", Toast.LENGTH_SHORT).show()
            } finally {
                // Reactivar el botón pase lo que pase
                btn.isEnabled = true
            }
        }
    }

    private fun abrirMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish() // Destruir LoginActivity para que el usuario no pueda volver atrás con el botón del teléfono
    }
}