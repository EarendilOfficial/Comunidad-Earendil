package com.earendil.app.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.earendil.app.R
import com.earendil.app.adapters.MensajesAdapter
import com.earendil.app.models.Mensaje
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MensajesFragment : Fragment(R.layout.fragment_mensajes) {

    private lateinit var etMensaje: EditText
    private lateinit var rvMensajes: RecyclerView
    private lateinit var adapter: MensajesAdapter
    private val listaMensajes = mutableListOf<Mensaje>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Vinculamos las vistas (Aquí estaba el error del Cast)
        etMensaje = view.findViewById(R.id.etMensaje)
        rvMensajes = view.findViewById(R.id.rvMensajes)
        val btnBuscar = view.findViewById<Button>(R.id.btnBuscar)
        val fabNuevoChat = view.findViewById<FloatingActionButton>(R.id.fabNuevoChat)

        // 2. Configuramos el RecyclerView con su Adapter
        adapter = MensajesAdapter(listaMensajes)  { mensaje ->
            Toast.makeText(
                requireContext(),
                "Click en: ${mensaje.texto}",
                Toast.LENGTH_SHORT
            ).show()
        } // TODO: Abrir el fragmento del chat

        rvMensajes.adapter = adapter

        // 3. Lógica de los botones
        btnBuscar.setOnClickListener {
            val texto = etMensaje.text.toString()
            if (texto.isNotEmpty()) {
                Toast.makeText(requireContext(), "Buscando: $texto", Toast.LENGTH_SHORT).show()
                // Aquí iría tu lógica de filtrado de Firebase
            }
        }

        fabNuevoChat.setOnClickListener {
            Toast.makeText(requireContext(), "Iniciando nuevo chat...", Toast.LENGTH_SHORT).show()
        }

        // 4. Cargamos los datos (puedes llamar aquí a tu lógica de Firebase)
        simularDatos()
    }

    private fun simularDatos() {
        listaMensajes.clear()
        listaMensajes.add(Mensaje("1", "¡Bienvenido a la comunidad Earendil!", "Sistema", System.currentTimeMillis()))
        listaMensajes.add(Mensaje("2", "Tengo un nuevo objeto para cambiar", "Samm", System.currentTimeMillis() - 3600000))
        listaMensajes.add(Mensaje("3", "La reunión es a las 8 PM", "Admin", System.currentTimeMillis() - 7200000))
        adapter.notifyDataSetChanged()
    }
}