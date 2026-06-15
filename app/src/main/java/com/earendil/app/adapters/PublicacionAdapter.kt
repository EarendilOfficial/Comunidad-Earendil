package com.earendil.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.earendil.app.R
import com.earendil.app.models.Publicacion

// import com.bumptech.glide.Glide // Descomenta si usas Glide

class PublicacionAdapter(
    private var lista: List<Publicacion>,
    private val onEditarClick: (Publicacion) -> Unit,
    private val onEliminarClick: (String) -> Unit
) : RecyclerView.Adapter<PublicacionAdapter.PostViewHolder>() {

    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitulo: TextView = view.findViewById(R.id.tvUsername) // Asegúrate de que coincida con tu XML de item
        val tvDescripcion: TextView = view.findViewById(R.id.tvPreview)
        val tvAutor: TextView = view.findViewById(R.id.tvUsername)
        val ivFoto: ImageView = view.findViewById(R.id.ivPublicacion)
        val btnEditar: ImageButton = view.findViewById(R.id.ibtnEdit)
        val btnEliminar: ImageButton = view.findViewById(R.id.ibtnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        // Inflamos el diseño de la tarjeta individual (item_publicacion.xml)
        // Si no tienes item_publicacion.xml, abajo te muestro cómo estructurarlo rápido
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_publicacion, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = lista[position]
        holder.tvTitulo.text = post.title
        holder.tvDescripcion.text = post.description
        holder.tvAutor.text = "Por: ${post.author}"

        // Controlar la imagen desde el servidor
        if (!post.imageUrl.isNullOrBlank()) {
            holder.ivFoto.visibility = View.VISIBLE
            val urlCompleta = "https://earendil.ceticlub.fun${post.imageUrl}"
            Glide.with(holder.itemView.context).load(urlCompleta).into(holder.ivFoto)
        } else {
            holder.ivFoto.visibility = View.GONE
        }

        // Configurar los botones delegando las acciones al Fragment
        holder.btnEditar.setOnClickListener { onEditarClick(post) }
        holder.btnEliminar.setOnClickListener { onEliminarClick(post._id) }
    }

    override fun getItemCount(): Int = lista.size

    // Función para actualizar los datos cuando busques o elimines un post
    fun actualizarLista(nuevaLista: List<Publicacion>) {
        this.lista = nuevaLista
        notifyDataSetChanged() // Le avisa al RecyclerView que se redibuje
    }
}