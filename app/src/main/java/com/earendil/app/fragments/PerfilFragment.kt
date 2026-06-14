package com.example.earendil.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.earendil.app.R
import com.earendil.app.fragments.LoginActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import kotlin.math.sqrt

class PerfilFragment : Fragment(R.layout.fragment_perfil), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var acelerometro: Sensor? = null
    private lateinit var imgQR: ImageView
    private lateinit var tvSensor: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val usuario = "FirebaseAuth.getInstance().currentUser"
        val uid = usuario ?: "usuario"

        view.findViewById<TextView>(R.id.tvNombre).text =
            "Nombre: ${usuario ?: "Jugador"}"

        view.findViewById<TextView>(R.id.tvCorreo).text =
            "Correo: ${usuario ?: "Sin correo"}"

        imgQR = view.findViewById(R.id.imgQR)
        tvSensor = view.findViewById(R.id.tvSensor)

        imgQR.setImageResource(0)
        tvSensor.text = "Agita el teléfono para mostrar tu código QR"


        sensorManager = requireContext().getSystemService(SensorManager::class.java)
        acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        view.findViewById<Button>(R.id.btnCerrarSesion).setOnClickListener {
            cerrarSesion();
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

        val fuerza = sqrt((x * x + y * y + z * z).toDouble())

        if (fuerza > 20) {
            tvSensor.text = "Código QR generado por movimiento del dispositivo"
            val uid = "FirebaseAuth.getInstance().currentUser?.uid" ?: "usuario"
            generarQR(uid)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun cerrarSesion() {
//        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(requireContext(), LoginActivity::class.java))
        requireActivity().finish()
    }

    private fun generarQR(texto: String) {
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
    }
}