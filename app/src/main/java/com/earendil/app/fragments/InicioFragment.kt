package com.earendil.app.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.earendil.app.R

class InicioFragment : Fragment(R.layout.fragment_inicio) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val usuario = "!"
        val tvBienvenida = view.findViewById<TextView>(R.id.tvBienvenida)
        val btnRecompensa = view.findViewById<Button>(R.id.btnRecompensa)
        val btnAlarma = view.findViewById<Button>(R.id.btnAlarma)

        tvBienvenida.text = "Bienvenido, ${usuario ?: "Jugador"}"

//        btnRecompensa.setOnClickListener {
//            NotificationHelper.mostrarNotificacion(
//                requireContext(),
//                "Recompensa reclamada",
//                "Has recibido monedas dentro de Comunidad MCE."
//            )
//        }

//        btnAlarma.setOnClickListener {
//
//        }
    }

}