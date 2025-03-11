package com.example.laboralkutxatpv

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.laboralkutxatpv.databinding.ActivityVentanaRealizarCompraBinding

class VentanaRealizarCompraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVentanaRealizarCompraBinding
    private lateinit var productoAdapter: ProductoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ajuste principal: usar la clase de Binding directamente
        binding = ActivityVentanaRealizarCompraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1) Lista de productos de ejemplo
        val listaProductos = listOf(
            Producto("The Legend of Zelda: Breath of the Wild", 0),
            Producto("Super Mario Odyssey", 0),
            Producto("Silla Gaming RGB", 0),
            Producto("Tarjeta Gráfica NVIDIA RTX 3080", 0),
            Producto("Ratón Inalámbrico Pro", 0),
            Producto("Teclado Mecánico RGB", 0)
        )

        // 2) Configurar el Adapter y el RecyclerView
        productoAdapter = ProductoAdapter(listaProductos.toMutableList())
        binding.recyclerViewProductos.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewProductos.adapter = productoAdapter

        // 3) Botón para Cancelar (vuelve atrás)
        binding.buttonCancelar.setOnClickListener {
            finish() // O bien onBackPressed()
        }

        // 4) Botón para Seleccionar método de pago
        binding.buttonSeleccionarPago.setOnClickListener {
            // Aquí puedes navegar a otra pantalla o mostrar un Toast, etc.
        }
    }
}
