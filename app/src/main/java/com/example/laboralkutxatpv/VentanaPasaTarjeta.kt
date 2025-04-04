package com.example.laboralkutxatpv

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.laboralkutxatpv.databinding.ActivityVentanaPasaTarjetaBinding

class VentanaPasaTarjeta : AppCompatActivity() {
    private lateinit var binding: ActivityVentanaPasaTarjetaBinding
    private var importe: Double = 0.0
    private var productosSeleccionados: ArrayList<Producto>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVentanaPasaTarjetaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        importe = intent.getDoubleExtra("montoTotal", 0.0)
        
        // Obtener productos seleccionados
        @Suppress("UNCHECKED_CAST")
        productosSeleccionados = intent.getSerializableExtra("productos") as? ArrayList<Producto>

        // Configurar la vista con los datos
        setupUI()

        // Configurar el botón cancelar
        binding.btnCancelar.setOnClickListener {
            finish() // Volver a la pantalla anterior
        }

        binding.btnAceptar.setOnClickListener {
            Toast.makeText(this, "¡Compra completada con éxito!", Toast.LENGTH_LONG).show()
            val intent = Intent(this, VentanaSeleccionarCupon::class.java)
            intent.putExtra("montoTotal", importe)
            intent.putExtra("metodoPago", "Tarjeta")
            
            // Pasar los productos seleccionados
            intent.putExtra("productos", productosSeleccionados)
            
            startActivity(intent)
        }
    }

    private fun setupUI() {
        binding.infoPasarTarjeta.text = "Aproxima, inserta o pasa la tarjeta por el datáfono"

        binding.importeLabel.text = "Importe"

        binding.importe.text = String.format("%.2f €", importe)
        // Configurar botones
        binding.btnCancelar.text = "Cancelar"
        binding.btnAceptar.text = "Aceptar"
    }
}