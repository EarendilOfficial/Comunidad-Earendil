package com.earendil.app.fragments

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.earendil.app.R
import com.earendil.app.adapters.ChatAdapter
import com.earendil.app.models.Mensaje
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ChatFragment : Fragment(R.layout.fragment_chat) {

    private lateinit var rvChat: RecyclerView
    private lateinit var etMessage: EditText
    private lateinit var btnSend: ImageButton
    private lateinit var adapter: ChatAdapter
    private val mensajes = mutableListOf<Mensaje>()
    private val db = FirebaseFirestore.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rvChat = view.findViewById(R.id.rvChat)
        etMessage = view.findViewById(R.id.etMessage)
        btnSend = view.findViewById(R.id.btnSend)

        adapter = ChatAdapter(mensajes)
        rvChat.adapter = adapter

        // ESCUCHAR TIEMPO REAL
        db.collection("chats").document("general").collection("mensajes")
            .orderBy("fecha", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    mensajes.clear()
                    for (doc in snapshot.documents) {
                        val m = doc.toObject(Mensaje::class.java)
                        if (m != null) mensajes.add(m)
                    }
                    adapter.notifyDataSetChanged()
                    rvChat.scrollToPosition(mensajes.size - 1)
                }
            }

        btnSend.setOnClickListener {
            val texto = etMessage.text.toString()
            if (texto.isNotBlank()) {
                val user = FirebaseAuth.getInstance().currentUser?.displayName ?: "Jugador"
                val nuevoMensaje = Mensaje(
                    id = "",
                    texto = texto,
                    autor = user,
                    fecha = System.currentTimeMillis()
                )

                db.collection("chats").document("general").collection("mensajes")
                    .add(nuevoMensaje)

                etMessage.text.clear()
            }
        }
    }
}