package com.earendil.app.models

data class Mensaje(
    var id: String = "",
    var texto: String = "",
    var autor: String = "",
    var fecha: Long = 0
)