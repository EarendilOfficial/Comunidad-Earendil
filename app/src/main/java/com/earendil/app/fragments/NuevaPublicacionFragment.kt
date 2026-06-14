package com.example.earendil.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.earendil.app.R


class NuevaPublicacionFragment : Fragment(R.layout.fragment_nueva_publicacion) {

//    private val db = FirebaseFirestore.getInstance()
    private val codigoCamara = 300

    private lateinit var etTitulo: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var imgFoto: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        etTitulo = view.findViewById(R.id.etTitulo)
        etDescripcion = view.findViewById(R.id.etDescripcion)
        imgFoto = view.findViewById(R.id.imgFoto)

        view.findViewById<Button>(R.id.btnCamara).setOnClickListener {
            abrirCamara()
        }

        view.findViewById<Button>(R.id.btnGuardar).setOnClickListener {
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
            imgFoto.setImageBitmap(foto)
        }
    }

    private fun guardarPublicacion() {
        val titulo = etTitulo.text.toString()
        val descripcion = etDescripcion.text.toString()

        if (titulo.isBlank() || descripcion.isBlank()) {
            Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val usuario = "FirebaseAuth.getInstance().currentUser"

        val datos = hashMapOf(
            "titulo" to titulo,
            "descripcion" to descripcion,
            "autor" to (usuario ?: "Jugador"),
            "fecha" to System.currentTimeMillis()
        )

//        db.collection("publicaciones")
//            .add(datos)
//            .addOnSuccessListener {
//                Toast.makeText(requireContext(), "Publicación guardada", Toast.LENGTH_SHORT).show()
//                etTitulo.text.clear()
//                etDescripcion.text.clear()
//                imgFoto.setImageResource(0)
//            }
    }
}