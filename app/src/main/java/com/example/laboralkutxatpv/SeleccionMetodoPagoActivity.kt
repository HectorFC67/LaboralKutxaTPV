package com.example.laboralkutxatpv

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.laboralkutxatpv.databinding.ActivitySeleccionMetodoPago2Binding
import com.example.laboralkutxatpv.databinding.ActivitySeleccionMetodoPagoBinding
import com.example.laboralkutxatpv.databinding.LayoutMetodoTarjetaBinding
import com.example.laboralkutxatpv.databinding.LayoutMetodoTransferenciaBinding
import com.example.laboralkutxatpv.databinding.LayoutMetodoBizumBinding
import com.example.laboralkutxatpv.databinding.LayoutMetodoEfectivoBinding

class SeleccionMetodoPagoActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySeleccionMetodoPago2Binding
    private var montoTotal: Double = 0.0
    private val REQUEST_CODE_INTRODUCIR_PAGO = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeleccionMetodoPago2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener el monto total pasado desde la pantalla anterior
        montoTotal = intent.getDoubleExtra("montoTotal", 0.0)
        binding.tvMontoTotal.text = "Total: ${String.format("%.2f", montoTotal)}€"

        // Configurar botón de cancelar
        binding.btnCancelar.setOnClickListener {
            finish()
        }

        // Configurar botón de efectivo
        binding.btnEfectivo.setOnClickListener() {
            val intent = Intent(this, QRCodeScannerActivity::class.java)
            // Pasar el importe final y el método de pago a la siguiente ventana
            intent.putExtra("montoTotal", montoTotal)
            intent.putExtra("metodoPago", "Efectivo")
            startActivity(intent)
        }

        // Configurar botón de tarjeta
        binding.btnTarjeta.setOnClickListener() {
            val intent = Intent(this, VentanaPasaTarjeta::class.java)
            // Pasar el importe final y el método de pago a la siguiente ventana
            intent.putExtra("montoTotal", montoTotal)
            startActivity(intent)
        }

        // Configurar botón de bizum
        binding.btnBizum.setOnClickListener() {
            val intent = Intent(this, IntroducirPago2::class.java)
            startActivityForResult(intent, REQUEST_CODE_INTRODUCIR_PAGO)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_INTRODUCIR_PAGO && resultCode == RESULT_OK) {
            val importe = data?.getDoubleExtra("importe", 0.0) ?: 0.0
            // Usar el importe...
        }
    }
} 