package com.example.laboralkutxatpv

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Repositorio para gestionar los productos de la aplicación.
 * Implementa el patrón Singleton para asegurar una única instancia.
 */
class ProductoRepository private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFS_FILENAME, Context.MODE_PRIVATE
    )
    private val gson = Gson()
    
    // Lista en memoria de productos
    private var productos: MutableList<ProductoCompleto> = mutableListOf()
    private val productoDao: ProductoDao = AppDatabase.getDatabase(context).productoDao()
    
    init {
        // Cargar productos guardados al iniciar
        cargarProductos()
    }
    
    companion object {
        private const val PREFS_FILENAME = "laboral_kutxa_products"
        private const val KEY_PRODUCTOS = "productos"
        
        @Volatile
        private var INSTANCE: ProductoRepository? = null
        
        fun getInstance(context: Context): ProductoRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ProductoRepository(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }
    
    /**
     * Obtiene la lista de todos los productos
     */
    suspend fun obtenerTodosLosProductos(): List<ProductoCompleto> {
        Log.d("TPVDebug", productoDao.obtenerTodosLosProductos().toString())
        return productoDao.obtenerTodosLosProductosCompletos()
    }

    suspend fun agregarProducto(producto: ProductoCompleto) : Long{
        val ultimoId = productoDao.obtenerUltimoId() ?: 0
        val productoConId = producto.copy(id = ultimoId + 1)

        Log.d("TPVDebug", "Agregando producto: $productoConId")

        productoDao.insertarProducto(productoConId)

        return productoConId.id
    }

    suspend fun eliminarProducto(id: Long): Boolean {
        val filasAfectadas = productoDao.eliminarProductoPorId(id)

        Log.d("TPVDebug", "Eliminado producto con ID: $id - Éxito: ${filasAfectadas > 0}")

        return filasAfectadas > 0
    }

    suspend fun actualizarProducto(producto: ProductoCompleto): Boolean {
        val filasAfectadas = productoDao.actualizarProducto(producto)

        Log.d("TPVDebug", "Actualizando producto: $producto - Éxito: ${filasAfectadas > 0}")

        return filasAfectadas > 0
    }
    /**
     * Convierte la lista de productos a productos simples para la pantalla de compra
     */
    fun obtenerProductosParaCompra(): List<Producto> {
        return productos.map { it.toProducto() }
    }

    /**
     * Obtiene un producto por su ID
     */
    fun obtenerProductoPorId(id: Long): ProductoCompleto? {
        return productos.find { it.id == id }
    }
    
    /**
     * Añade un nuevo producto
     */
    //fun agregarProducto(producto: ProductoCompleto): Long {
        // Si el ID es 0, generamos uno nuevo
    //    val productoConId = if (producto.id == 0L) {
    //       producto.copy(id = generarNuevoId())
    //    } else {
    //        producto
    //    }

    //    productos.add(productoConId)
    //    guardarProductos()
    //    return productoConId.id
    //}
    
    /**
     * Actualiza un producto existente
     */
    //fun actualizarProducto(producto: ProductoCompleto): Boolean {
    //    val index = productos.indexOfFirst { it.id == producto.id }
    //    if (index != -1) {
    //        productos[index] = producto
    //        guardarProductos()
    //        return true
    //    }
    //    return false
    //}
    
    /**
     * Elimina un producto
     */
    //fun eliminarProducto(id: Long): Boolean {
    //    val eliminado = productos.removeIf { it.id == id }
    //    if (eliminado) {
    //        guardarProductos()
    //    }
    //    return eliminado
    //}
    
    /**
     * Guardar productos en SharedPreferences
     */
    private fun guardarProductos() {
        val productosJson = gson.toJson(productos)
        sharedPreferences.edit().putString(KEY_PRODUCTOS, productosJson).apply()
    }
    
    /**
     * Cargar productos desde SharedPreferences
     */
    private fun cargarProductos() {
        val productosJson = sharedPreferences.getString(KEY_PRODUCTOS, null)
        if (productosJson != null) {
            val tipo = object : TypeToken<List<ProductoCompleto>>() {}.type
            val productosGuardados: List<ProductoCompleto> = gson.fromJson(productosJson, tipo)
            productos.clear()
            productos.addAll(productosGuardados)
        } else {
            // Si no hay productos guardados, cargamos los datos de ejemplo
            cargarDatosDeEjemplo()
        }
    }
    
    /**
     * Genera un nuevo ID único para un producto
     */
    private fun generarNuevoId(): Long {
        return if (productos.isEmpty()) {
            1
        } else {
            (productos.maxOf { it.id } + 1)
        }
    }
    
    /**
     * Carga datos de ejemplo (solo se usa la primera vez)
     */
    private fun cargarDatosDeEjemplo() {
        val productosEjemplo = listOf(
            ProductoCompleto(1, "The Legend of Zelda: Breath of the Wild", 59.99, 15, 
                "El mejor juego de aventuras para Nintendo Switch"),
            ProductoCompleto(2, "Super Mario Odyssey", 49.99, 20, 
                "Plataformas 3D con Mario"),
            ProductoCompleto(3, "Silla Gaming RGB", 159.99, 5, 
                "Cómoda silla para largas sesiones de juego"),
            ProductoCompleto(4, "Tarjeta Gráfica NVIDIA RTX 3080", 899.99, 3, 
                "Potente tarjeta gráfica para gaming de última generación"),
            ProductoCompleto(5, "Ratón Inalámbrico Pro", 69.99, 30, 
                "Ratón ergonómico con alta precisión"),
            ProductoCompleto(6, "Teclado Mecánico RGB", 109.99, 12, 
                "Teclado mecánico con retroiluminación personalizable")
        )
        
        productos.addAll(productosEjemplo)
        guardarProductos()
    }
}