package com.example.laboralkutxatpv

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.laboralkutxatpv.databinding.ActivityVentanaRealizarCompraBinding
import java.text.NumberFormat
import java.util.Locale

class VentanaRealizarCompraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVentanaRealizarCompraBinding
    private lateinit var productoAdapter: ProductoAdapter
    private lateinit var productoRepository: ProductoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ajuste principal: usar la clase de Binding directamente
        binding = ActivityVentanaRealizarCompraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar el repositorio
        productoRepository = ProductoRepository.getInstance(applicationContext)

        // Obtener productos del repositorio
        val listaProductos = productoRepository.obtenerProductosParaCompra()

        // Configurar el Adapter y el RecyclerView
        productoAdapter = ProductoAdapter(
            productos = listaProductos.toMutableList(),
            onTotalChanged = { total -> actualizarTotal(total) }
        )
        binding.recyclerViewProductos.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewProductos.adapter = productoAdapter

        // Inicializar el total al cargar la actividad
        actualizarTotal(0.0)

        // Botón para Cancelar (vuelve atrás)
        binding.buttonCancelar.setOnClickListener {
            finish()
        }

        // Botón para Seleccionar método de pago
        binding.buttonSeleccionarPago.setOnClickListener {
            // Verificar que haya al menos un producto seleccionado
            val montoTotal = productoAdapter.calcularTotal()
            
            if (montoTotal <= 0.0) {
                Toast.makeText(this, "Debe seleccionar al menos un producto", Toast.LENGTH_SHORT).show()
            } else {
                // Navegar a la pantalla de selección de método de pago
                val intent = Intent(this, SeleccionMetodoPagoActivity::class.java).apply {
                    putExtra("montoTotal", montoTotal)
                }
                startActivity(intent)
            }
        }
    }

    /**
     * Actualiza la visualización del total de la compra con formato de moneda
     * @param total el monto total a mostrar
     */
    private fun actualizarTotal(total: Double) {
        val formatoMoneda = NumberFormat.getCurrencyInstance(Locale("es", "ES"))
        binding.textViewTotalCompra.text = formatoMoneda.format(total)
    }
}
