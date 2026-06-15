package com.earendil.app.adapters

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.earendil.app.R
import com.earendil.app.models.Mensaje
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth

class ChatAdapter(private val mensajes: List<Mensaje>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    val miUsuario = FirebaseAuth.getInstance().currentUser?.displayName ?: "Anónimo"

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: MaterialCardView = view.findViewById(R.id.cardMensaje)
        val tvAutor: TextView = view.findViewById(R.id.tvAutor)
        val tvTexto: TextView = view.findViewById(R.id.tvTexto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val msg = mensajes[position]
        holder.tvAutor.text = msg.autor
        holder.tvTexto.text = msg.texto

        val params = holder.card.layoutParams as ConstraintLayout.LayoutParams
        if (msg.autor == miUsuario) {
            // Mensaje Mío: Derecha y color azulito
            params.horizontalBias = 1.0f
            holder.card.setCardBackgroundColor(holder.itemView.context.getColor(R.color.azulito))
            holder.tvAutor.visibility = View.GONE // No necesito ver mi nombre
        } else {
            // Mensaje de Otros: Izquierda y color gris
            params.horizontalBias = 0.0f
            holder.card.setCardBackgroundColor(holder.itemView.context.getColor(R.color.gris))
            holder.tvAutor.visibility = View.VISIBLE
        }
        holder.card.layoutParams = params
    }

    override fun getItemCount() = mensajes.size
}