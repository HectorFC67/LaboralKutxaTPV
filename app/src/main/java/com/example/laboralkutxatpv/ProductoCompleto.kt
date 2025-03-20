package com.example.laboralkutxatpv

import java.io.Serializable

data class ProductoCompleto(
    val id: Long = 0,
    var nombre: String,
    var precio: Double,
    var stock: Int = 0,
    var descripcion: String = "",
    // El campo cantidad se usa durante la compra, pero no en la gestión
    var cantidad: Int = 0
) : Serializable {
    // Constructor de conversión desde el modelo simple Producto
    constructor(producto: Producto) : this(
        nombre = producto.nombre,
        precio = producto.precio,
        cantidad = producto.cantidad
    )

    // Método para convertir a Producto simple para la pantalla de compra
    fun toProducto(): Producto {
        return Producto(
            nombre = nombre,
            cantidad = cantidad,
            precio = precio,
            stockDisponible = stock
        )
    }
}