package com.example.laboralkutxatpv

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.laboralkutxatpv.databinding.ActivityVentanaSeleccionarCuponBinding

class VentanaSeleccionarCupon : AppCompatActivity() {
    private lateinit var binding: ActivityVentanaSeleccionarCuponBinding
    private var importe: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVentanaSeleccionarCuponBinding.inflate(layoutInflater)
        setContentView(binding.root)

        importe = intent.getDoubleExtra("montoTotal", 0.0)

        // Configurar la vista con los datos
        setupUI()

        // Configurar el botón cancelar
        binding.btnCancelar.setOnClickListener {
            finish() // Volver a la pantalla anterior
        }

        // Configurar el botón continuar
        binding.btnAceptar.setOnClickListener {
            // Aquí puedes implementar la lógica para finalizar la compra
            // Por ejemplo, guardar la transacción en una base de datos

            Toast.makeText(this, "¡Trabajando en esto!", Toast.LENGTH_LONG).show()

        }
    }

    private fun setupUI() {
        binding.infoSeleccionarCupon.text = "Selecciona el cupón que desee aplicar a su compra"

        binding.tituloSeleccionCupon.text = "Selección de Cupones"

        binding.radioDescuento1.text = "10€ de Descuento"
        binding.radioDescuento2.text = "20% de Descuento"
        binding.radioNinguno.text = "Ningún Cupón"

        // Configurar botones
        binding.btnCancelar.text = "Cancelar"
        binding.btnAceptar.text = "Aceptar"
    }
}