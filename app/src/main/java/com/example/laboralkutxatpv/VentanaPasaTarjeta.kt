package com.example.laboralkutxatpv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.laboralkutxatpv.databinding.ActivityVentanaPasaTarjetaBinding

class VentanaPasaTarjeta : AppCompatActivity() {
    private lateinit var binding: ActivityVentanaPasaTarjetaBinding
    private var importe: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVentanaPasaTarjetaBinding.inflate(layoutInflater)
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

            Toast.makeText(this, "¡Compra completada con éxito!", Toast.LENGTH_LONG).show()

            // Volver a la pantalla principal
            finishAffinity() // Cierra todas las actividades y vuelve a la pantalla principal
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