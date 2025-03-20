package com.example.laboralkutxatpv

import java.io.Serializable

data class Producto(
    val nombre: String,
    var cantidad: Int,
    val precio: Double,
    val stockDisponible: Int = 0
) : Serializable
