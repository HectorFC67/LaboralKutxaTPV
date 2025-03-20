package com.example.laboralkutxatpv

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.laboralkutxatpv.databinding.ActivityIntroducirPago2Binding
import java.text.NumberFormat
import java.util.Locale

class IntroducirPago2 : AppCompatActivity() {
    private lateinit var binding: ActivityIntroducirPago2Binding
    private var importeActual: String = "0"
    private var hayComa: Boolean = false
    private var decimales: Int = 0
    private val formatoMoneda = NumberFormat.getCurrencyInstance(Locale("es", "ES"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroducirPago2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBotonesNumericos()
        setupBotonesControl()
        actualizarDisplay()
    }

    private fun setupBotonesNumericos() {
        // Configurar botones numéricos (0-9)
        val botonesNumericos = arrayOf(
            binding.btn0, binding.btn1, binding.btn2, binding.btn3, binding.btn4,
            binding.btn5, binding.btn6, binding.btn7, binding.btn8, binding.btn9
        )

        botonesNumericos.forEachIndexed { index, boton ->
            boton.setOnClickListener { agregarNumero(index) }
        }

        // Configurar botón coma
        binding.btnComa.setOnClickListener { agregarComa() }

        // Configurar botón F (ahora borra último dígito)
        binding.btnF.setOnClickListener { borrarUltimoDigito() }
    }

    private fun setupBotonesControl() {
        // Botón rojo (Cancelar)
        binding.btnCancelar.apply {
            setOnClickListener {
                setResult(RESULT_CANCELED)
                finish()
            }
        }

        // Botón amarillo (Borrar todo)
        binding.btnBorrar.apply {
            setOnClickListener { borrarTodo() }
        }

        // Botón verde (Aceptar)
        binding.btnAceptar.apply {
            setOnClickListener { aceptarImporte() }
        }
    }

    private fun agregarNumero(numero: Int) {
        if (hayComa) {
            val parteDecimal = importeActual.substring(importeActual.indexOf(",") + 1)
            if (parteDecimal == "00") {
                // Si tenemos ,00 reemplazamos el primer 0
                importeActual = importeActual.substring(0, importeActual.length - 2) + numero.toString() + "0"
            } else if (parteDecimal.length == 2 && parteDecimal.endsWith("0")) {
                // Si el segundo dígito es 0, lo reemplazamos
                importeActual = importeActual.substring(0, importeActual.length - 1) + numero.toString()
            } else if (parteDecimal.length == 2) {
                // Si ya tenemos dos decimales, reemplazamos el último
                importeActual = importeActual.substring(0, importeActual.length - 1) + numero.toString()
            }
        } else {
            if (importeActual == "0") {
                importeActual = numero.toString()
            } else {
                importeActual += numero.toString()
            }
        }
        actualizarDisplay()
    }

    private fun agregarComa() {
        if (!hayComa) {
            hayComa = true
            importeActual += ",00"
            actualizarDisplay()
        }
    }

    private fun borrarUltimoDigito() {
        if (hayComa) {
            val parteDecimal = importeActual.substring(importeActual.indexOf(",") + 1)
            if (parteDecimal.endsWith("0")) {
                // Si termina en 0, quitamos la coma y los decimales
                importeActual = importeActual.substring(0, importeActual.indexOf(","))
                hayComa = false
            } else {
                // Si no termina en 0, reemplazamos el último número por 0
                importeActual = importeActual.substring(0, importeActual.length - 1) + "0"
            }
        } else if (importeActual.length > 1) {
            importeActual = importeActual.substring(0, importeActual.length - 1)
        } else {
            importeActual = "0"
        }
        actualizarDisplay()
    }

    private fun borrarTodo() {
        importeActual = "0"
        hayComa = false
        decimales = 0
        actualizarDisplay()
    }

    private fun actualizarDisplay() {
        try {
            // Convertimos la cadena reemplazando la coma por punto para el parsing
            val numeroStr = if (hayComa) importeActual.replace(",", ".") else importeActual
            val numero = numeroStr.toDouble()
            
            if (hayComa) {
                // Si hay coma, mostramos los decimales (usando coma)
                val parteEntera = numero.toInt()
                val parteDecimal = importeActual.substring(importeActual.indexOf(",") + 1)
                binding.tvImporte.text = String.format("%d,%s €", parteEntera, parteDecimal)
            } else {
                // Si no hay coma, mostramos solo enteros
                binding.tvImporte.text = String.format("%d €", numero.toInt())
            }
        } catch (e: NumberFormatException) {
            binding.tvImporte.text = "0 €"
        }
    }

    private fun aceptarImporte() {
        try {
            // Convertimos la cadena reemplazando la coma por punto para el parsing
            val numeroStr = if (hayComa) importeActual.replace(",", ".") else importeActual
            val importe = numeroStr.toDouble()
            val intent = Intent()
            intent.putExtra("importe", importe)
            setResult(RESULT_OK, intent)
            finish()
        } catch (e: NumberFormatException) {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}