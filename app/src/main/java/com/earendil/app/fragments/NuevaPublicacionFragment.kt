package com.earendil.app.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.earendil.app.R
import com.earendil.app.rest.RetrofitClient // Ajusta a tu ruta
import com.earendil.app.session.SessionManager // Ajusta a tu ruta
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class NuevaPublicacionFragment : Fragment(R.layout.fragment_nueva_publicacion) {

    private val codigoCamara = 300
    private lateinit var etTitulo: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var imgFoto: ImageView
    private lateinit var btnPublicar: Button

    private var fotoBitmap: Bitmap? = null // Guardamos la referencia aquí globalmente

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        etTitulo = view.findViewById(R.id.etTitulo)
        etDescripcion = view.findViewById(R.id.etDescripcion)
        imgFoto = view.findViewById(R.id.ivPreview)
        btnPublicar = view.findViewById(R.id.btnPublicar)

        view.findViewById<Button>(R.id.btnCamara).setOnClickListener {
            abrirCamara()
        }

        btnPublicar.setOnClickListener {
            guardarPublicacion()
        }
    }

    private fun abrirCamara() {
        if (androidx.core.content.ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 301)
            return
        }

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, codigoCamara)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == codigoCamara && resultCode == Activity.RESULT_OK) {
            val foto = data?.extras?.get("data") as? Bitmap
            if (foto != null) {
                fotoBitmap = foto
                imgFoto.setImageBitmap(fotoBitmap)
            }
        }
    }

    private fun guardarPublicacion() {
        val titulo = etTitulo.text.toString().trim()
        val descripcion = etDescripcion.text.toString().trim()

        if (titulo.isBlank() || descripcion.isBlank()) {
            Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Bloquear el botón para evitar múltiples envíos
        btnPublicar.isEnabled = false

        lifecycleScope.launch {
            try {
                // 1. Inicializar el ApiService usando el SessionManager para el Token JWT
                val sessionManager = SessionManager(requireContext())
                val apiService = RetrofitClient.create { sessionManager.fetchAuthToken() }

                // 2. Convertir Strings a RequestBody normales para Multipart
                val titleBody = titulo.toRequestBody("text/plain".toMediaTypeOrNull())
                val descBody = descripcion.toRequestBody("text/plain".toMediaTypeOrNull())

                // 3. Convertir el Bitmap en un MultipartBody.Part binario si el usuario se tomó una foto
                var imagePart: MultipartBody.Part? = null

                fotoBitmap?.let { bitmap ->
                    val stream = ByteArrayOutputStream()
                    // Comprimimos el bitmap a JPEG al 80% de calidad para no saturar la red
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
                    val byteArray = stream.toByteArray()

                    val imageRequestBody = byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull(), 0, byteArray.size)

                    // "image" es el mismo nombre que pusimos en upload.single('image') en Node.js
                    imagePart = MultipartBody.Part.createFormData("image", "upload_post.jpg", imageRequestBody)
                }

                // 4. Enviar datos al Servidor
                val response = apiService.crearPublicacion(titleBody, descBody, imagePart)

                if (response.isSuccessful && response.body()?.successful == true) {
                    Toast.makeText(requireContext(), "¡Publicación creada exitosamente!", Toast.LENGTH_SHORT).show()

                    // Limpiar interfaz
                    etTitulo.text.clear()
                    etDescripcion.text.clear()
                    imgFoto.setImageResource(0)
                    fotoBitmap = null
                } else {
                    val errorReason = response.body()?.reason ?: "Error del servidor"
                    Toast.makeText(requireContext(), "Error: $errorReason", Toast.LENGTH_LONG).show()
                }

            } catch (e: Exception) {
                Log.e("NuevaPublicacion", "Error al enviar post: ${e.message}")
                Toast.makeText(requireContext(), "No se pudo conectar con el servidor", Toast.LENGTH_SHORT).show()
            } finally {
                btnPublicar.isEnabled = true
            }
        }
    }
}