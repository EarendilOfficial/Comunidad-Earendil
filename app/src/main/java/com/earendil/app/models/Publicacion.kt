package com.earendil.app.models

data class Publicacion(
    val _id: String,          // Mongo usa _id en lugar de id
    val title: String,
    val description: String,
    val author: String,
    val imageUrl: String?,    // Puede venir sin imagen
    val createdAt: String     // Fecha en formato String ISO o Timestamp
)