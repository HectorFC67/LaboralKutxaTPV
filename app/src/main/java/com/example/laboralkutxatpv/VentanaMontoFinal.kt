package com.example.laboralkutxatpv

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.laboralkutxatpv.databinding.ActivityVentanaMontoFinalBinding

class VentanaMontoFinal : AppCompatActivity() {

    private lateinit var binding: ActivityVentanaMontoFinalBinding
    private var montoTotal: Double = 0.0
    private var metodoPago: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVentanaMontoFinalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener datos del intent
        montoTotal = intent.getDoubleExtra("montoTotal", 0.0)
        metodoPago = intent.getStringExtra("metodoPago") ?: "No especificado"

        // Configurar la vista con los datos
        setupUI()

        // Configurar el botón cancelar
        binding.btnCancelar.setOnClickListener {
            finish() // Volver a la pantalla anterior
        }

        // Configurar el botón continuar
        binding.btnContinuar.setOnClickListener {
            val intent = Intent(this, VentanaConfirmacionPago::class.java)
            startActivity(intent)
        }
    }

    private fun setupUI() {
        // Configurar el título
        binding.tvTitulo.text = "Resumen de Operación"
        
        // Calcular el descuento (en este ejemplo, 0% de descuento)
        val descuentoPorcentaje = 0.0
        val montoDescuento = montoTotal * (descuentoPorcentaje / 100)
        val montoFinal = montoTotal - montoDescuento

        // Configurar los valores en la vista
        binding.tvMontoInicialLabel.text = "Monto Inicial:"
        binding.tvMontoInicial.text = String.format("%.2f €", montoTotal)
        
        binding.tvDescuentoLabel.text = "Descuento ($descuentoPorcentaje%):"
        binding.tvDescuento.text = String.format("%.2f €", montoDescuento)
        
        binding.tvMontoFinalLabel.text = "Monto Final ($metodoPago):"
        binding.tvMontoFinal.text = String.format("%.2f €", montoFinal)
        
        // Configurar botón
        binding.btnContinuar.text = "Finalizar Compra"
    }
}