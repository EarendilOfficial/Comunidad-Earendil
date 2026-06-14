package com.earendil.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

//    private lateinit var auth: FirebaseAuth
    private val requestCodeGoogle = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

//        if (auth.currentUser != null) {
//            abrirMain()
//            return
//        }

        findViewById<Button>(R.id.btnGoogle).setOnClickListener {
//          iniciarSesionGoogle()
            abrirMain()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        abrirMain()

        return;
    }

//    private fun guardarUsuario() {
//        val usuario = "auth.currentUser" ?: return
//
//        val datos = hashMapOf(
//            "uid" to usuario.uid,
//            "nombre" to (usuario.displayName ?: "Jugador"),
//            "correo" to (usuario.email ?: ""),
//            "nivel" to 1
//        )

//        FirebaseFirestore.getInstance()
//            .collection("usuarios")
//            .document(usuario.uid)
//            .set(datos)
//    }

    private fun abrirMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}