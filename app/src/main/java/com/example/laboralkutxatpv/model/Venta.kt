package com.example.laboralkutxatpv.model

import java.util.Date

/**
 * Clase que representa una venta completa
 */
data class Venta(
    val id: Long = 0,
    val fecha: Date,
    val montoTotal: Double,
    val metodoPago: String,
    val descuentoAplicado: Double = 0.0,
    val cuponUtilizado: String = "Ninguno",
    val productos: List<ProductoVendido> = emptyList()
)

/**
 * Clase que representa un producto incluido en una venta
 */
data class ProductoVendido(
    val id: Long = 0,
    val ventaId: Long = 0,
    val nombreProducto: String,
    val precioUnitario: Double,
    val cantidad: Int,
    val subtotal: Double
)