package com.earendil.app.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.earendil.app.R

object NotificationHelper {

    private const val CHANNEL_ID = "canal_earendil"
    private const val PREFS = "historial_notificaciones"
    private const val KEY = "lista"

    fun mostrarNotificacion(context: Context, titulo: String, mensaje: String) {
        guardarNotificacion(context, titulo, mensaje)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permiso = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            )

            if (permiso != PackageManager.PERMISSION_GRANTED) {
                return
            }
        }

        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                CHANNEL_ID,
                "Notificaciones Earendil",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(canal)
        }

        val notificacion = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(titulo)
            .setContentText(mensaje)
            .setAutoCancel(true)
            .build()

        manager.notify(System.currentTimeMillis().toInt(), notificacion)
    }

    private fun guardarNotificacion(context: Context, titulo: String, mensaje: String) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val actual = prefs.getString(KEY, "") ?: ""

        val nueva = "$titulo|$mensaje|${System.currentTimeMillis()}"
        val actualizado = if (actual.isBlank()) nueva else "$nueva;;$actual"

        prefs.edit().putString(KEY, actualizado).apply()
    }

    fun obtenerHistorial(context: Context): List<String> {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val actual = prefs.getString(KEY, "") ?: ""

        if (actual.isBlank()) return emptyList()

        return actual.split(";;").map { item ->
            val partes = item.split("|")
            val titulo = partes.getOrNull(0) ?: "Notificación"
            val mensaje = partes.getOrNull(1) ?: ""
            val fecha = partes.getOrNull(2)?.toLongOrNull() ?: 0L

            "$titulo\n$mensaje\n${java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(java.util.Date(fecha))}"
        }
    }
}