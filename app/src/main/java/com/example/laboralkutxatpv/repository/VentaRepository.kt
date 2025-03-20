package com.example.laboralkutxatpv.repository

import com.example.laboralkutxatpv.model.ProductoVendido
import com.example.laboralkutxatpv.model.Venta
import java.util.Calendar
import java.util.Date

/**
 * Repositorio para manejar datos de ventas
 * Implementación como singleton para mantener las ventas en memoria
 */
class VentaRepository private constructor() {
    
    // Lista en memoria para almacenar las ventas
    private val ventasRegistradas = mutableListOf<Venta>()
    private var nextId: Long = 1
    
    init {
        // Ya no cargaremos datos de ejemplo por defecto
        // Solo inicializamos el ID para nuevas ventas
        nextId = 1
    }
    
    /**
     * Obtiene la lista de ventas registradas
     */
    fun obtenerVentas(): List<Venta> {
        return ventasRegistradas.sortedByDescending { it.fecha }
    }
    
    /**
     * Registra una nueva venta
     */
    fun registrarVenta(venta: Venta): Venta {
        val ventaConId = venta.copy(id = nextId)
        nextId++
        ventasRegistradas.add(ventaConId)
        return ventaConId
    }
    
    /**
     * Filtra ventas por fecha
     */
    fun filtrarPorFecha(fechaInicio: Date, fechaFin: Date): List<Venta> {
        val calendarFin = Calendar.getInstance().apply {
            time = fechaFin
            // Ajustar fecha fin para incluir todo el día
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }
        
        return obtenerVentas().filter { venta -> 
            !venta.fecha.before(fechaInicio) && !venta.fecha.after(calendarFin.time)
        }
    }
    
    /**
     * Filtra ventas por método de pago
     */
    fun filtrarPorMetodoPago(metodoPago: String): List<Venta> {
        return if (metodoPago.equals("Todos", true)) {
            obtenerVentas()
        } else {
            obtenerVentas().filter { it.metodoPago.equals(metodoPago, true) }
        }
    }
    
    /**
     * Genera datos de ejemplo para mostrar en la aplicación
     */
    private fun generarDatosEjemplo(): List<Venta> {
        val ventas = mutableListOf<Venta>()
        val calendar = Calendar.getInstance()
        
        // Venta 1
        val productos1 = listOf(
            ProductoVendido(1, 1, "Café solo", 1.20, 2, 2.40),
            ProductoVendido(2, 1, "Croissant", 1.50, 1, 1.50)
        )
        
        ventas.add(
            Venta(
                id = 1,
                fecha = calendar.time,
                montoTotal = 3.90,
                metodoPago = "Efectivo",
                cuponUtilizado = "Ninguno",
                productos = productos1
            )
        )
        
        // Venta 2
        calendar.add(Calendar.HOUR, -3)
        val productos2 = listOf(
            ProductoVendido(3, 2, "Menú del día", 12.0, 1, 12.0),
            ProductoVendido(4, 2, "Refresco", 2.0, 1, 2.0),
            ProductoVendido(5, 2, "Postre", 3.0, 1, 3.0)
        )
        
        ventas.add(
            Venta(
                id = 2,
                fecha = calendar.time,
                montoTotal = 17.0,
                metodoPago = "Tarjeta",
                cuponUtilizado = "Ninguno",
                productos = productos2
            )
        )
        
        // Venta 3
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        val productos3 = listOf(
            ProductoVendido(6, 3, "Hamburguesa", 9.50, 2, 19.0),
            ProductoVendido(7, 3, "Patatas", 3.0, 1, 3.0),
            ProductoVendido(8, 3, "Cerveza", 2.50, 2, 5.0)
        )
        
        ventas.add(
            Venta(
                id = 3,
                fecha = calendar.time,
                montoTotal = 27.0,
                metodoPago = "Bizum",
                descuentoAplicado = 2.70,
                cuponUtilizado = "VERANO10",
                productos = productos3
            )
        )
        
        // Venta 4
        calendar.add(Calendar.DAY_OF_MONTH, -2)
        val productos4 = listOf(
            ProductoVendido(9, 4, "Pizza", 12.0, 1, 12.0)
        )
        
        ventas.add(
            Venta(
                id = 4,
                fecha = calendar.time,
                montoTotal = 12.0,
                metodoPago = "Efectivo",
                cuponUtilizado = "Ninguno",
                productos = productos4
            )
        )
        
        // Venta 5
        calendar.add(Calendar.DAY_OF_MONTH, -3)
        val productos5 = listOf(
            ProductoVendido(10, 5, "Ensalada", 7.50, 1, 7.50),
            ProductoVendido(11, 5, "Agua", 1.50, 1, 1.50)
        )
        
        ventas.add(
            Venta(
                id = 5,
                fecha = calendar.time,
                montoTotal = 9.0,
                metodoPago = "Tarjeta",
                cuponUtilizado = "Ninguno",
                productos = productos5
            )
        )
        
        return ventas
    }
    
    companion object {
        @Volatile
        private var instance: VentaRepository? = null
        
        fun getInstance(): VentaRepository {
            return instance ?: synchronized(this) {
                instance ?: VentaRepository().also { instance = it }
            }
        }
    }
}

/**
 * Clase para estadísticas de ventas
 */
data class EstadisticasVentas(
    val cantidadVentas: Int,
    val montoTotal: Double
)