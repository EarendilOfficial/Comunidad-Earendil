package com.example.earendil.fragments

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.earendil.app.R
import com.example.comunidadmce.utils.NotificationHelper

class MensajesFragment : Fragment(R.layout.fragment_mensajes) {

//    private val db = FirebaseFirestore.getInstance()
    private lateinit var etMensaje: EditText
    private lateinit var contenedor: LinearLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        etMensaje = view.findViewById(R.id.etMensaje)
        contenedor = view.findViewById(R.id.contenedorMensajes)

        view.findViewById<Button>(R.id.btnEnviar).setOnClickListener {
            enviarMensaje()
        }

        cargarMensajes()
    }

    private fun enviarMensaje() {
        val texto = etMensaje.text.toString()

        if (texto.isBlank()) {
            Toast.makeText(requireContext(), "Escribe un mensaje", Toast.LENGTH_SHORT).show()
            return
        }

        val usuario = "FirebaseAuth.getInstance().currentUser"

        val datos = hashMapOf(
            "texto" to texto,
            "autor" to (usuario ?: "Jugador"),
            "fecha" to System.currentTimeMillis()
        )

//        db.collection("mensajes")
//            .add(datos)
//            .addOnSuccessListener {
//                etMensaje.text.clear()
//                NotificationHelper.mostrarNotificacion(
//                    requireContext(),
//                    "Nuevo mensaje",
//                    "Se envió un mensaje correctamente."
//                )
//            }
    }

    private fun cargarMensajes() {
//        db.collection("mensajes")
//            .addSnapshotListener { resultado, _ ->
//                contenedor.removeAllViews()
//
//                resultado?.forEach { doc ->
//                    val mensaje = Mensaje(
//                        id = doc.id,
//                        texto = doc.getString("texto") ?: "",
//                        autor = doc.getString("autor") ?: "",
//                        fecha = doc.getLong("fecha") ?: 0
//                    )
//
//                    val tv = TextView(requireContext())
//                    tv.text = "${mensaje.autor}: ${mensaje.texto}"
//                    tv.textSize = 16f
//                    tv.setPadding(10, 10, 10, 10)
//
//                    contenedor.addView(tv)
//                }
//            }
    }
}