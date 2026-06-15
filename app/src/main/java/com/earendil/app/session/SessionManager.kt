package com.earendil.app.session

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    // Crea un archivo XML privado dentro del teléfono llamado "app_session"
    private val prefs: SharedPreferences = context.getSharedPreferences("app_session", Context.MODE_PRIVATE)

    /**
     * Guarda el token recibido de Node.js
     */
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString("USER_TOKEN", token)
        editor.apply() // apply() guarda de forma asíncrona (es más rápido)
    }

    /**
     * Recupera el token. Devuelve null si no hay sesión iniciada.
     */
    fun fetchAuthToken(): String? {
        return prefs.getString("USER_TOKEN", null)
    }

    /**
     * Borra el token (para usarlo en el Logout)
     */
    fun clearSession() {
        val editor = prefs.edit()
        editor.remove("USER_TOKEN")
        editor.apply()
    }
}