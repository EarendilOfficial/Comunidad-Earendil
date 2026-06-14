package com.earendil.app.fragments

import android.Manifest
import android.content.Intent
import android.location.Location
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresPermission
import androidx.fragment.app.Fragment
import com.earendil.app.R

class MasFragment : Fragment(R.layout.fragment_mas) {

    private lateinit var tvResultado: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tvResultado = view.findViewById(R.id.tvResultado)

        view.findViewById<Button>(R.id.btnNotificaciones).setOnClickListener {
//            NotificationHelper.mostrarNotificacion(
//                requireContext(),
//                "Panel de notificaciones",
//                "Notificación generada desde Comunidad MCE."
//            )
        }

//        view.findViewById<Button>(R.id.btnUbicacion).setOnClickListener {
//            obtenerUbicacion()
//        }

        view.findViewById<Button>(R.id.btnAudio).setOnClickListener {
            reproducirAudio()
        }

        view.findViewById<Button>(R.id.btnMensajes).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedorFragments, MensajesFragment())
                .addToBackStack(null)
                .commit()
        }
    }

//    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
//    private fun obtenerUbicacion() {
//        val cliente = LocationServices.getFusedLocationProviderClient(requireContext())
//
//        cliente.lastLocation.addOnSuccessListener { location: Location? ->
//            if (location != null) {
//                tvResultado.text =
//                    "Latitud: ${location.latitude}\nLongitud: ${location.longitude}"
//
//                val uri = Uri.parse("geo:${location.latitude},${location.longitude}?q=${location.latitude},${location.longitude}")
//                val intent = Intent(Intent.ACTION_VIEW, uri)
//                startActivity(intent)
//            } else {
//                tvResultado.text = "No se pudo obtener ubicación."
//            }
//        }
//    }

    private fun reproducirAudio() {
        val sonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val ringtone = RingtoneManager.getRingtone(requireContext(), sonido)
        ringtone.play()
        tvResultado.text = "Recurso multimedia de audio reproducido."
    }
}