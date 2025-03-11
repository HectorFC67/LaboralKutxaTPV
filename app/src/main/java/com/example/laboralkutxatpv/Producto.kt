package com.example.laboralkutxatpv

data class Producto(
    val nombre: String,
    var cantidad: Int,
    val precio: Double,
    val stockDisponible: Int = 0
)
