package com.example.laboralkutxatpv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.laboralkutxatpv.databinding.ActivityVentanaConfirmacionPagoBinding

class VentanaConfirmacionPago : AppCompatActivity() {

    private lateinit var binding: ActivityVentanaConfirmacionPagoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflamos el layout usando View Binding
        binding = ActivityVentanaConfirmacionPagoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Botón "Cancelar"
        binding.btnCancelar.setOnClickListener {
            // Acción al pulsar "Cancelar"
            finish() // Cierra esta ventana y vuelve a la anterior
        }

        // Botón "Continuar"
        binding.btnContinuar.setOnClickListener {
            // Acción al pulsar "Continuar"
            // Por ejemplo, cerrar esta ventana
            finishAffinity()
        }
    }
}
