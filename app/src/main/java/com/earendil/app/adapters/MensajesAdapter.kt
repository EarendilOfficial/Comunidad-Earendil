package com.earendil.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.earendil.app.R
import com.example.comunidadmce.models.Mensaje
import java.text.SimpleDateFormat
import java.util.*

class MensajesAdapter(private var mensajes: List<Mensaje>) :
    RecyclerView.Adapter<MensajesAdapter.MensajeViewHolder>() {

    class MensajeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUsername: TextView = view.findViewById(R.id.tvUsername)
        val tvPreview: TextView = view.findViewById(R.id.tvPreview)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MensajeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mensajes, parent, false)
        return MensajeViewHolder(view)
    }

    override fun onBindViewHolder(holder: MensajeViewHolder, position: Int) {
        val mensaje = mensajes[position]
        holder.tvUsername.text = mensaje.autor
        holder.tvPreview.text = mensaje.texto
        
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        holder.tvDate.text = sdf.format(Date(mensaje.fecha))
    }

    override fun getItemCount() = mensajes.size

    fun updateData(newMensajes: List<Mensaje>) {
        mensajes = newMensajes
        notifyDataSetChanged()
    }
}