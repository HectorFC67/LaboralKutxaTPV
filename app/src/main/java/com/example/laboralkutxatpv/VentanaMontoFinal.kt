package com.example.laboralkutxatpv

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.laboralkutxatpv.databinding.ActivityVentanaMontoFinalBinding
import java.text.NumberFormat
import java.util.Locale

class VentanaMontoFinal : AppCompatActivity() {

    private lateinit var binding: ActivityVentanaMontoFinalBinding
    private var montoTotal: Double = 0.0
    private var montoFinal: Double = 0.0
    private var montoDescuento: Double = 0.0
    private var descuentoPorcentaje: Double = 0.0
    private var descripcionDescuento: String = "Sin descuento"
    private var metodoPago: String = "No especificado"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVentanaMontoFinalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener datos del intent
        obtenerDatosIntent()

        // Configurar la vista con los datos
        setupUI()
        
        // Animar los valores
        animarValores()

        // Configurar el botón cancelar
        binding.btnCancelar.setOnClickListener {
            finish() // Volver a la pantalla anterior
        }

        // Configurar el botón continuar
        binding.btnContinuar.setOnClickListener {
            val intent = Intent(this, VentanaConfirmacionPago::class.java)
            // Pasar el importe final y el método de pago a la siguiente ventana
            intent.putExtra("montoTotal", montoFinal)
            intent.putExtra("metodoPago", metodoPago)
            startActivity(intent)
        }
    }
    
    private fun obtenerDatosIntent() {
        // Obtener todos los datos posibles del intent
        montoTotal = intent.getDoubleExtra("montoTotal", 0.0)
        montoFinal = intent.getDoubleExtra("montoFinal", montoTotal) // Si no hay final, usar el total
        
        // Obtener detalles del descuento
        montoDescuento = intent.getDoubleExtra("montoDescuento", 0.0)
        descuentoPorcentaje = intent.getDoubleExtra("descuentoPorcentaje", 0.0)
        descripcionDescuento = intent.getStringExtra("descripcionDescuento") ?: "Sin descuento"
        metodoPago = intent.getStringExtra("metodoPago") ?: "No especificado"
        
        // Si no recibimos el monto de descuento pero tenemos porcentaje, calcularlo
        if (montoDescuento == 0.0 && descuentoPorcentaje > 0) {
            montoDescuento = montoTotal * (descuentoPorcentaje / 100)
            montoFinal = montoTotal - montoDescuento
        }
        
        // Si tenemos monto de descuento pero no porcentaje, calcular el porcentaje
        if (montoDescuento > 0 && descuentoPorcentaje == 0.0 && montoTotal > 0) {
            descuentoPorcentaje = (montoDescuento / montoTotal) * 100
        }
    }

    private fun setupUI() {
        val formatoMoneda = NumberFormat.getCurrencyInstance(Locale("es", "ES"))
        
        // Configurar el título
        binding.tvTitulo.text = "Resumen de Operación"
        
        // Configurar los valores en la vista
        binding.tvMontoInicialLabel.text = "Monto Inicial:"
        binding.tvMontoInicial.text = formatoMoneda.format(montoTotal)
        
        // Configurar el descuento con la descripción

        binding.tvDescuentoLabel.text = "Descuento:"
        binding.tvDescuento.text = "${formatoMoneda.format(montoDescuento)}"
        
        // Re   saltar el descuento si existe
        if (montoDescuento > 0) {
            binding.tvDescuento.setTextColor(getColor(android.R.color.holo_green_dark))
        }
        
        // Configurar el monto final con el método de pago
        binding.tvMontoFinalLabel.text = "Monto Final:"
        binding.tvMontoFinal.text = "${formatoMoneda.format(montoFinal)}"
        
        // Configurar botón
        binding.btnContinuar.text = "Finalizar Compra"
    }
    
    private fun animarValores() {
        // Aplicar animaciones a los importes
        val animacionEntrada = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        animacionEntrada.duration = 500
        
        binding.tvMontoInicial.startAnimation(animacionEntrada)
        
        // Retrasar las demás animaciones para efecto secuencial
        binding.tvDescuento.postDelayed({
            binding.tvDescuento.startAnimation(animacionEntrada)
        }, 300)
        
        binding.tvMontoFinal.postDelayed({
            binding.tvMontoFinal.startAnimation(animacionEntrada)
        }, 600)
    }
}