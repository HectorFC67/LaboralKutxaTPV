package com.example.laboralkutxatpv

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Producto")
data class Producto(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    val id: Long = 0,
    @ColumnInfo(name = "Nombre")
    val nombre: String,
    @ColumnInfo(name = "Cantidad")
    var cantidad: Int,
    @ColumnInfo(name = "Precio")
    val precio: Double,
    @ColumnInfo(name = "Stock")
    val stockDisponible: Int = 0,
    @ColumnInfo(name = "Descripcion")
    val descripcion: String
) : Serializable
