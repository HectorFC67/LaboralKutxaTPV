package com.example.laboralkutxatpv

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Producto")
data class ProductoCompleto(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    val id: Long = 0,
    @ColumnInfo(name = "Nombre")
    var nombre: String,
    @ColumnInfo(name = "Precio")
    var precio: Double,
    @ColumnInfo(name = "Stock")
    var stock: Int = 0,
    @ColumnInfo(name = "Descripcion")
    var descripcion: String = "",
    // El campo cantidad se usa durante la compra, pero no en la gestión
    @ColumnInfo(name = "Cantidad")
    var cantidad: Int = 0
) : Serializable {
    // Constructor de conversión desde el modelo simple Producto
    constructor(producto: Producto) : this(
        id = producto.id,
        nombre = producto.nombre,
        precio = producto.precio,
        cantidad = producto.cantidad,
        descripcion = producto.descripcion
    )

    // Método para convertir a Producto simple para la pantalla de compra
    fun toProducto(): Producto {
        return Producto(
            id = id,
            nombre = nombre,
            cantidad = cantidad,
            precio = precio,
            stockDisponible = stock,
            descripcion = descripcion
        )
    }
}