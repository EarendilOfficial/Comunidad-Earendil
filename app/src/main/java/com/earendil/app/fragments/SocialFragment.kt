package com.earendil.app.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.earendil.app.R
import com.earendil.app.adapters.PublicacionAdapter
import com.earendil.app.models.Publicacion
import com.earendil.app.rest.UpdatePostRequest
import com.earendil.app.rest.RetrofitClient
import com.earendil.app.session.SessionManager
import kotlinx.coroutines.launch

class SocialFragment : Fragment(R.layout.fragment_social) {

    private lateinit var rvPublicaciones: RecyclerView
    private lateinit var etBuscar: EditText
    private lateinit var adaptador: PublicacionAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 1. Vincular los componentes reales de tu XML
        rvPublicaciones = view.findViewById(R.id.rvPublicaciones)
        etBuscar = view.findViewById(R.id.etBuscar)

        // 2. Inicializar el Adapter con una lista vacía y pasarle las funciones de clic
        adaptador = PublicacionAdapter(
            lista = emptyList(),
            onEditarClick = { post -> mostrarDialogoEditar(post) },
            onEliminarClick = { id -> eliminarPublicacion(id) }
        )
        rvPublicaciones.adapter = adaptador // ¡Aquí se asocia el adapter!

        // 3. Configurar el botón buscar
        view.findViewById<Button>(R.id.btnBuscar).setOnClickListener {
            cargarPublicaciones(etBuscar.text.toString().trim())
        }

        // 4. Configurar tu FloatingActionButton para ir a crear publicaciones
        view.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabNuevaPublicacion)
            .setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.contenedorFragments, NuevaPublicacionFragment())
                    .addToBackStack(null) // Permite volver atrás al presionar el botón de retroceso del cel
                    .commit()
            }

        // Carga inicial
        cargarPublicaciones("")
    }

    private fun cargarPublicaciones(filtro: String) {
        lifecycleScope.launch {
            try {
                val sessionManager = SessionManager(requireContext())
                val apiService = RetrofitClient.create { sessionManager.fetchAuthToken() }

                val queryParam = if (filtro.isBlank()) null else filtro
                val response = apiService.getPosts(queryParam)

                if (response.isSuccessful && response.body()?.successful == true) {
                    val listaPosts = response.body()?.posts ?: emptyList()

                    // Le enviamos los posts nuevos al adaptador para que los dibuje
                    adaptador.actualizarLista(listaPosts)

                    if (listaPosts.isEmpty()) {
                        Toast.makeText(requireContext(), "No se encontraron publicaciones", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val reason = response.body()?.reason ?: "Error al descargar contenido."
                    Toast.makeText(requireContext(), reason, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("SocialFragment", "Error: ${e.message}")
                Toast.makeText(requireContext(), "Error de conexión con el servidor", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun mostrarDialogoEditar(publicacion: Publicacion) {
        val input = EditText(requireContext()).apply {
            setText(publicacion.description)
            // 1. Cambia el color del texto que escribe el usuario
            setTextColor(android.graphics.Color.BLACK)
            // O usa un recurso de color: setTextColor(androidx.core.content.ContextCompat.getColor(context, R.color.tu_color))
        }

        val dialogo = AlertDialog.Builder(requireContext())
            .setTitle("Modificar publicación")
            .setView(input)
            .setPositiveButton("Guardar") { _, _ ->
                val nuevaDesc = input.text.toString().trim()
                if (nuevaDesc.isNotBlank()) {
                    actualizarDescripcion(publicacion._id, nuevaDesc)
                }
            }
            .setNegativeButton("Cancelar", null)
            .create() // Usamos create() en lugar de show() directo

        dialogo.show() // Mostramos el diálogo primero

        // 2. Cambia el color de los botones (Debe hacerse después de show())
        dialogo.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(android.graphics.Color.BLUE)
        dialogo.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(android.graphics.Color.RED)
    }


    private fun actualizarDescripcion(id: String, nuevaDescripcion: String) {
        lifecycleScope.launch {
            try {
                val sessionManager = SessionManager(requireContext())
                val apiService = RetrofitClient.create { sessionManager.fetchAuthToken() }

                val response = apiService.updatePost(id, UpdatePostRequest(nuevaDescripcion))
                if (response.isSuccessful && response.body()?.successful == true) {
                    Toast.makeText(requireContext(), "Publicación actualizada", Toast.LENGTH_SHORT).show()
                    cargarPublicaciones(etBuscar.text.toString()) // Recargar manteniendo el filtro actual
                } else {
                    Toast.makeText(requireContext(), "No tienes permisos para editar este post", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error de red", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun eliminarPublicacion(id: String) {
        lifecycleScope.launch {
            try {
                val sessionManager = SessionManager(requireContext())
                val apiService = RetrofitClient.create { sessionManager.fetchAuthToken() }

                val response = apiService.deletePost(id)
                if (response.isSuccessful && response.body()?.successful == true) {
                    Toast.makeText(requireContext(), "Publicación eliminada", Toast.LENGTH_SHORT).show()
                    cargarPublicaciones("") // Resetear la vista limpia
                } else {
                    Toast.makeText(requireContext(), "No tienes permisos para eliminar este post", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error de red", Toast.LENGTH_SHORT).show()
            }
        }
    }
}