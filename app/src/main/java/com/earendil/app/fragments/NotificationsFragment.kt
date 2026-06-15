package com.earendil.app.fragments

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.earendil.app.R
import com.earendil.app.utils.NotificationHelper

class NotificationsFragment : Fragment(R.layout.fragment_notifications) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val lista = view.findViewById<ListView>(R.id.listaNotificaciones)

        val datos = NotificationHelper.obtenerHistorial(requireContext())

        lista.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            if (datos.isEmpty()) listOf("No hay notificaciones registradas.") else datos
        )
    }
}