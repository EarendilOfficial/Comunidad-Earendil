package com.earendil.app.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.earendil.app.R
import com.earendil.app.LoginActivity
import com.earendil.app.rest.RetrofitClient
import com.earendil.app.session.SessionManager
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.launch
import kotlin.math.sqrt

class PerfilFragment : Fragment(R.layout.fragment_perfil), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var acelerometro: Sensor? = null

    private lateinit var tvNombre: TextView
    private lateinit var tvCorreo: TextView
    private lateinit var imgQR: ImageView
    private lateinit var tvSensor: TextView
    private lateinit var tvQROculto: TextView
    private lateinit var sessionManager: SessionManager

    private var textoParaQR: String = "Usuario_Earendil" // Valor por defecto si falla la red

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 1. Instanciar vistas y componentes
        tvNombre = view.findViewById(R.id.tvNombre)
        tvCorreo = view.findViewById(R.id.tvCorreo)
        imgQR = view.findViewById(R.id.imgQR)
        tvSensor = view.findViewById(R.id.tvSensor)
        tvQROculto = view.findViewById(R.id.tvQROculto)

        sessionManager = SessionManager(requireContext())

        // 2. Cargar la información real desde el Servidor Node.js
        obtenerDatosPerfil()

        // 3. Inicializar el Acelerómetro (Sensor de Movimiento)
        sensorManager = requireContext().getSystemService(SensorManager::class.java)
        acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        if (acelerometro == null) {
            tvSensor.text = "El dispositivo no cuenta con acelerómetro."
        }

        view.findViewById<Button>(R.id.btnCerrarSesion).setOnClickListener {
            cerrarSesion()
        }
    }

    private fun obtenerDatosPerfil() {
        lifecycleScope.launch {
            try {
                val apiService = RetrofitClient.create { sessionManager.fetchAuthToken() }
                val response = apiService.getMyUserData() // Llama a tu endpoint de Node.js

                if (response.isSuccessful && response.body() != null) {
                    val userSafeData = response.body()

                    // Supongo que tu respuesta contiene 'username', 'mail' y su 'uit'
                    val username = userSafeData?.username ?: "Jugador"
                    val email = userSafeData?.mail ?: "Sin correo"
                    val uitCode = userSafeData?.uit ?: username

                    // Pintar en la interfaz
                    tvNombre.text = "Usuario: $username"
                    tvCorreo.text = "Correo: $email"

                    // Asignamos el código UIT único para cifrarlo en el QR al agitar
                    textoParaQR = uitCode
                }
            } catch (e: Exception) {
                Log.e("PerfilFragment", "Error cargando perfil: ${e.message}")
                // Dejar los valores predeterminados del XML si no hay internet
            }
        }
    }

    override fun onResume() {
        super.onResume()
        acelerometro?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return

        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        // Calcular la fuerza vectorial del movimiento (Fórmula de Magnitud)
        val fuerza = sqrt((x * x + y * y + z * z).toDouble())

        // El valor de la gravedad terrestre es ~9.8, un movimiento brusco supera los 20
        if (fuerza > 21) {
            tvSensor.text = "¡Código de acceso generado correctamente!"
            tvQROculto.visibility = View.GONE // Remueve el texto "[ QR OCULTO ]" de la pantalla

            generarQR(textoParaQR)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun cerrarSesion() {
        // 1. Limpiar credenciales locales
        sessionManager.clearSession()

        // 2. Forzar salida limpia limpiando el historial de vistas
        val intent = Intent(requireContext(), LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        requireActivity().finish()
    }

    private fun generarQR(texto: String) {
        try {
            val writer = QRCodeWriter()
            val bitMatrix = writer.encode(texto, BarcodeFormat.QR_CODE, 500, 500)
            val bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.RGB_565)

            for (x in 0 until 500) {
                for (y in 0 until 500) {
                    bitmap.setPixel(
                        x,
                        y,
                        if (bitMatrix[x, y]) android.graphics.Color.BLACK
                        else android.graphics.Color.WHITE
                    )
                }
            }
            imgQR.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Log.e("QR_ERROR", "No se pudo codificar el texto: ${e.message}")
        }
    }
}