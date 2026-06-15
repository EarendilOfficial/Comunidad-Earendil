package com.earendil.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.earendil.app.R
import com.earendil.app.models.Mensaje
import java.text.SimpleDateFormat
import java.util.*

class MensajesAdapter(
    private var mensajes: List<Mensaje>,
    private var onItemClick: (Mensaje) -> Unit
) : RecyclerView.Adapter<MensajesAdapter.MensajeViewHolder>() {

    inner class MensajeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvImage: ImageView = view.findViewById(R.id.ivProfile)
        val tvUsername: TextView = view.findViewById(R.id.tvUsername)
        val tvPreview: TextView = view.findViewById(R.id.tvPreview)
        val tvDate: TextView = view.findViewById(R.id.tvDate)

        fun bind(mensaje: Mensaje) {
            tvImage.setImageResource(R.drawable.circulo_notificacion) // TODO: Change this later for the sender profile picture
            tvUsername.text = mensaje.autor; // Autor
            tvPreview.text = mensaje.texto; // Texto Preview
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            tvDate.text = sdf.format(Date(mensaje.fecha)) // Fecha

            itemView.setOnClickListener {
                onItemClick(mensaje)
            } // Listener para los clicks
        } // bind
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MensajeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mensajes, parent, false)
        return MensajeViewHolder(view)
    }

    override fun onBindViewHolder(holder: MensajeViewHolder, position: Int) {
        val mensaje = mensajes[position]
        holder.bind(mensaje)
    }

    override fun getItemCount() = mensajes.size

    fun updateData(newMensajes: List<Mensaje>) {
        mensajes = newMensajes
        notifyDataSetChanged()
    }
}