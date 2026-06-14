package com.earendil.app.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.comunidadmce.models.Publicacion
import com.earendil.app.R

class SocialFragment : Fragment(R.layout.fragment_social) {

//    private val db = FirebaseFirestore.getInstance()
    private lateinit var contenedor: LinearLayout
    private lateinit var etBuscar: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        contenedor = view.findViewById(R.id.contenedorPublicaciones)
        etBuscar = view.findViewById(R.id.etBuscar)

        view.findViewById<Button>(R.id.btnBuscar).setOnClickListener {
            cargarPublicaciones(etBuscar.text.toString())
        }

        cargarPublicaciones("")
    }

    private fun cargarPublicaciones(filtro: String) {
//        db.collection("publicaciones")
//            .get()
//            .addOnSuccessListener { resultado ->
//                contenedor.removeAllViews()
//
//                for (doc in resultado) {
//                    val publicacion = Publicacion(
//                        id = doc.id,
//                        titulo = doc.getString("titulo") ?: "",
//                        descripcion = doc.getString("descripcion") ?: "",
//                        autor = doc.getString("autor") ?: "",
//                        fecha = doc.getLong("fecha") ?: 0
//                    )
//
//                    if (filtro.isBlank() ||
//                        publicacion.titulo.contains(filtro, true) ||
//                        publicacion.descripcion.contains(filtro, true)
//                    ) {
//                        agregarVistaPublicacion(publicacion)
//                    }
//                }
//            }
    }

//    private fun agregarVistaPublicacion(publicacion: Publicacion) {
//        val tarjeta = LinearLayout(requireContext())
//        tarjeta.orientation = LinearLayout.VERTICAL
//        tarjeta.setPadding(20, 20, 20, 20)
//
//        val titulo = TextView(requireContext())
//        titulo.text = publicacion.titulo
//        titulo.textSize = 20f
//        titulo.setTypeface(null, android.graphics.Typeface.BOLD)
//
//        val descripcion = TextView(requireContext())
//        descripcion.text = publicacion.descripcion
//
//        val autor = TextView(requireContext())
//        autor.text = "Autor: ${publicacion.autor}"
//
//        val btnEditar = Button(requireContext())
//        btnEditar.text = "Editar"
//
//        val btnEliminar = Button(requireContext())
//        btnEliminar.text = "Eliminar"
//
//        btnEditar.setOnClickListener {
//            mostrarDialogoEditar(publicacion)
//        }
//
//        btnEliminar.setOnClickListener {
//            db.collection("publicaciones")
//                .document(publicacion.id)
//                .delete()
//                .addOnSuccessListener {
//                    Toast.makeText(requireContext(), "Publicación eliminada", Toast.LENGTH_SHORT).show()
//                    cargarPublicaciones("")
//                }
//        }
//
//        tarjeta.addView(titulo)
//        tarjeta.addView(descripcion)
//        tarjeta.addView(autor)
//        tarjeta.addView(btnEditar)
//        tarjeta.addView(btnEliminar)
//
//        contenedor.addView(tarjeta)
//    }

    private fun mostrarDialogoEditar(publicacion: Publicacion) {
        val input = EditText(requireContext())
        input.setText(publicacion.descripcion)

        AlertDialog.Builder(requireContext())
            .setTitle("Modificar publicación")
            .setView(input)
            .setPositiveButton("Guardar") { _, _ ->
//                db.collection("publicaciones")
//                    .document(publicacion.id)
//                    .update("descripcion", input.text.toString())
//                    .addOnSuccessListener {
//                        cargarPublicaciones("")
//                    }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}