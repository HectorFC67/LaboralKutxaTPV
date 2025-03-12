package com.example.laboralkutxatpv

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.laboralkutxatpv.databinding.ActivitySeleccionMetodoPagoBinding
import com.example.laboralkutxatpv.databinding.LayoutMetodoTarjetaBinding
import com.example.laboralkutxatpv.databinding.LayoutMetodoTransferenciaBinding
import com.example.laboralkutxatpv.databinding.LayoutMetodoBizumBinding
import com.example.laboralkutxatpv.databinding.LayoutMetodoEfectivoBinding

class SeleccionMetodoPagoActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySeleccionMetodoPagoBinding
    private var montoTotal: Double = 0.0
    
    // Variables para los layouts específicos de cada método de pago
    private var layoutTarjeta: View? = null
    private var layoutTransferencia: View? = null
    private var layoutBizum: View? = null
    private var layoutEfectivo: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeleccionMetodoPagoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener el monto total pasado desde la pantalla anterior
        montoTotal = intent.getDoubleExtra("montoTotal", 0.0)
        binding.tvMontoTotal.text = "Total: ${String.format("%.2f", montoTotal)}€"

        // Inicializar los layouts de métodos de pago
        inicializarLayoutsMetodosPago()

        // Configurar los listeners para los radio buttons
        setupRadioListeners()

        // Configurar botón de cancelar
        binding.btnCancelar.setOnClickListener {
            finish()
        }

        // Configurar botón de continuar
        binding.btnContinuar.setOnClickListener {
            val radioSeleccionadoId = binding.radioGroupMetodosPago.checkedRadioButtonId
            
            if (radioSeleccionadoId == -1) {
                // Ningún método de pago seleccionado
                Toast.makeText(this, "Por favor, seleccione un método de pago", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Método de pago seleccionado
            val radioSeleccionado = findViewById<RadioButton>(radioSeleccionadoId)
            val metodoPago = radioSeleccionado.text.toString()
            
            // Validar campos según el método de pago
            if (!validarCampos(radioSeleccionadoId)) {
                return@setOnClickListener
            }

            if (metodoPago == "Tarjeta de crédito/débito") {
                // Crear un Intent para pasar a la pantalla de pasar la tarjeta
                val intent = Intent(this, VentanaPasaTarjeta::class.java).apply {
                    putExtra("montoTotal", montoTotal)
                    putExtra("metodoPago", metodoPago)
                }
                startActivity(intent)
            }

            else if (metodoPago == "Efectivo") {
                // Crear un Intent para pasar a la pantalla de monto final
                val intent = Intent(this, VentanaMontoFinal::class.java).apply {
                    putExtra("montoTotal", montoTotal)
                    putExtra("metodoPago", metodoPago)
                }
                startActivity(intent)
            }

        }
    }

    private fun inicializarLayoutsMetodosPago() {
        // Inflar los layouts de cada método de pago
        val inflater = LayoutInflater.from(this)
        
        layoutTarjeta = inflater.inflate(R.layout.layout_metodo_tarjeta, null)
        layoutTransferencia = inflater.inflate(R.layout.layout_metodo_transferencia, null)
        layoutBizum = inflater.inflate(R.layout.layout_metodo_bizum, null)
        layoutEfectivo = inflater.inflate(R.layout.layout_metodo_efectivo, null)
    }

    private fun setupRadioListeners() {
        binding.radioGroupMetodosPago.setOnCheckedChangeListener { _, checkedId ->
            // Limpiar el contenedor
            binding.frameLayoutMetodoPago.removeAllViews()
            
            // Mostrar el layout correspondiente según el método de pago seleccionado
            when (checkedId) {
                R.id.radioTarjeta -> {
                    binding.frameLayoutMetodoPago.addView(layoutTarjeta)
                }
                R.id.radioTransferencia -> {
                    binding.frameLayoutMetodoPago.addView(layoutTransferencia)
                }
                R.id.radioBizum -> {
                    binding.frameLayoutMetodoPago.addView(layoutBizum)
                }
                R.id.radioEfectivo -> {
                    binding.frameLayoutMetodoPago.addView(layoutEfectivo)
                }
            }
        }
        
        // Seleccionar el efectivo por defecto
        binding.radioEfectivo.isChecked = true
    }

    private fun validarCampos(radioSeleccionadoId: Int): Boolean {
        when (radioSeleccionadoId) {
            R.id.radioTarjeta -> {
                // Validar campos de tarjeta
                val tarjetaView = layoutTarjeta ?: return false
                val numeroTarjeta = tarjetaView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etNumeroTarjeta).text.toString()
                val fechaExpiracion = tarjetaView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etFechaExpiracion).text.toString()
                val cvv = tarjetaView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etCVV).text.toString()
                val titular = tarjetaView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etTitular).text.toString()
                
                if (numeroTarjeta.length < 16) {
                    Toast.makeText(this, "Introduce un número de tarjeta válido", Toast.LENGTH_SHORT).show()
                    return false
                }
                if (fechaExpiracion.length < 5) {
                    Toast.makeText(this, "Introduce una fecha de expiración válida (MM/AA)", Toast.LENGTH_SHORT).show()
                    return false
                }
                if (cvv.length < 3) {
                    Toast.makeText(this, "Introduce un CVV válido", Toast.LENGTH_SHORT).show()
                    return false
                }
                if (titular.isEmpty()) {
                    Toast.makeText(this, "Introduce el nombre del titular", Toast.LENGTH_SHORT).show()
                    return false
                }
            }
            R.id.radioTransferencia -> {
                // Validar campos de transferencia
                val transferenciaView = layoutTransferencia ?: return false
                val referencia = transferenciaView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etReferencia).text.toString()
                
                if (referencia.isEmpty()) {
                    Toast.makeText(this, "Introduce una referencia para la transferencia", Toast.LENGTH_SHORT).show()
                    return false
                }
            }
            R.id.radioBizum -> {
                // Validar campos de Bizum
                val bizumView = layoutBizum ?: return false
                val telefono = bizumView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etTelefono).text.toString()
                
                if (telefono.length < 9) {
                    Toast.makeText(this, "Introduce un número de teléfono válido", Toast.LENGTH_SHORT).show()
                    return false
                }
            }
        }
        
        return true
    }
} 