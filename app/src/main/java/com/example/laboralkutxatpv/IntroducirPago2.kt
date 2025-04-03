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
    
    // Variables para operaciones matemáticas
    private var valorAlmacenado: Double = 0.0
    private var operacionActual: String = ""
    private var empezarNuevoNumero: Boolean = true

    // Variables para operaciones de descuento
    private var mostrandoDescuento: Boolean = false
    private var importeOriginal: Double = 0.0

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

        // Configurar botón F (descuentos rápidos)
        binding.btnF.apply {
            // Hacerlo más visible como botón de función
            text = "DTO %"
            
            // Al hacer clic mostramos un diálogo con las opciones de descuento
            setOnClickListener { mostrarOpcionesDescuento() }
        }
    }

    private fun setupBotonesControl() {
        // Botón rojo (Cancelar)
        binding.btnCancelar.apply {
            setOnClickListener {
                setResult(RESULT_CANCELED)
                finish()
            }
        }

        // Botón amarillo (Borrar)
        binding.btnBorrar.apply {
            // Clic corto: borrar último dígito
            setOnClickListener { borrarUltimoDigito() }
            
            // Clic largo: borrar todo
            setOnLongClickListener { 
                borrarTodo()
                true
            }
        }

        // Botón verde (Aceptar)
        binding.btnAceptar.apply {
            setOnClickListener { aceptarImporte() }
        }
    }

    private fun agregarNumero(numero: Int) {
        // Si estamos empezando un nuevo número después de una operación
        if (empezarNuevoNumero) {
            importeActual = "0"
            hayComa = false
            decimales = 0
            empezarNuevoNumero = false
        }
        
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
            val intent = Intent(this, SeleccionMetodoPagoActivity::class.java).apply {
                putExtra("montoTotal", importe)
            }
            startActivity(intent)
        } catch (e: NumberFormatException) {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    private fun mostrarOpcionesDescuento() {
        // Si ya estamos mostrando un descuento, volver al importe original
        if (mostrandoDescuento) {
            // Restaurar el importe original
            val importeStr = importeOriginal.toString().replace(".", ",")
            importeActual = importeStr
            hayComa = importeStr.contains(",")
            mostrandoDescuento = false
            actualizarDisplay()
            return
        }
        
        // Guardar el importe actual como original
        val numeroStr = if (hayComa) importeActual.replace(",", ".") else importeActual
        importeOriginal = numeroStr.toDouble()
        
        // Crear un diálogo con las opciones de descuento
        val opciones = arrayOf("5% descuento", "10% descuento", "15% descuento", "20% descuento", "Personalizado")
        
        android.app.AlertDialog.Builder(this)
            .setTitle("Aplicar descuento")
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> aplicarDescuentoPorcentaje(5.0)
                    1 -> aplicarDescuentoPorcentaje(10.0)
                    2 -> aplicarDescuentoPorcentaje(15.0)
                    3 -> aplicarDescuentoPorcentaje(20.0)
                    4 -> mostrarDescuentoPersonalizado()
                }
            }
            .show()
    }
    
    private fun aplicarDescuentoPorcentaje(porcentaje: Double) {
        val descuento = importeOriginal * (porcentaje / 100.0)
        val importeConDescuento = importeOriginal - descuento
        
        // Formatear y mostrar el resultado con 2 decimales
        val importeStr = String.format("%.2f", importeConDescuento).replace(".", ",")
        importeActual = importeStr
        hayComa = true
        mostrandoDescuento = true
        
        actualizarDisplay()
        
        // Mostrar un mensaje con el descuento aplicado
        android.widget.Toast.makeText(
            this,
            "Descuento del ${porcentaje.toInt()}% aplicado: -${String.format("%.2f", descuento)}€",
            android.widget.Toast.LENGTH_SHORT
        ).show()
    }
    
    private fun mostrarDescuentoPersonalizado() {
        // Crear un diálogo para introducir el porcentaje personalizado
        val dialogView = android.widget.EditText(this).apply {
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
            hint = "Introduce % (ej: 7.5)"
        }
        
        android.app.AlertDialog.Builder(this)
            .setTitle("Descuento personalizado")
            .setView(dialogView)
            .setPositiveButton("Aplicar") { _, _ ->
                try {
                    val porcentaje = dialogView.text.toString().toDouble()
                    if (porcentaje > 0 && porcentaje <= 100) {
                        aplicarDescuentoPorcentaje(porcentaje)
                    } else {
                        android.widget.Toast.makeText(
                            this,
                            "Por favor, introduce un valor entre 0 y 100",
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    android.widget.Toast.makeText(
                        this,
                        "Valor no válido",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}