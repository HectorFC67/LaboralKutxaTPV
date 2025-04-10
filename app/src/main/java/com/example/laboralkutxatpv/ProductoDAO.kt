package com.example.laboralkutxatpv

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductoDao {

    // Insertar un nuevo producto
    @Insert
    suspend fun insertarProducto(producto: ProductoCompleto): Long

    // Insertar múltiples productos
    @Insert
    suspend fun insertarProductos(vararg productos: ProductoCompleto)

    // Actualizar un producto existente
    @Update
    suspend fun actualizarProducto(producto: ProductoCompleto)

    // Eliminar un producto
    @Delete
    suspend fun eliminarProducto(producto: ProductoCompleto)

    // Obtener todos los productos
    @Query("SELECT * FROM Producto")
    suspend fun obtenerTodosLosProductosCompletos(): List<ProductoCompleto>

    @Query("SELECT * FROM Producto")
    suspend fun obtenerTodosLosProductos(): List<Producto>

    // Obtener un producto por su ID
    @Query("SELECT * FROM Producto WHERE ID = :id")
    suspend fun obtenerProductoPorId(id: Long): ProductoCompleto?

    // Obtener productos con cantidad mayor a 0
    @Query("SELECT * FROM Producto WHERE Stock > 0")
    suspend fun obtenerProductosDisponibles(): List<ProductoCompleto>

    // Filtrar productos por nombre
    @Query("SELECT * FROM Producto WHERE Nombre LIKE :nombre")
    suspend fun buscarProductoPorNombre(nombre: String): List<ProductoCompleto>
}
