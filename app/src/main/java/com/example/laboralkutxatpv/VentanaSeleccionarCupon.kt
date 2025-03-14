package com.example.laboralkutxatpv

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.laboralkutxatpv.databinding.ActivityVentanaSeleccionarCuponBinding
import java.text.NumberFormat
import java.util.Locale

class VentanaSeleccionarCupon : AppCompatActivity() {
    private lateinit var binding: ActivityVentanaSeleccionarCuponBinding
    private var importeOriginal: Double = 0.0
    private var importeFinal: Double = 0.0
    private var descuentoSeleccionado: TipoDescuento = TipoDescuento.NINGUNO
    private var metodoPago: String = "No especificado"
    
    // Enum para manejar los tipos de descuento
    enum class TipoDescuento {
        FIJO_10, PORCENTAJE_20, NINGUNO
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVentanaSeleccionarCuponBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intentar obtener el importe de diferentes claves posibles
        importeOriginal = obtenerImporteDesdeDiferentesFuentes()
        importeFinal = importeOriginal // Por defecto, sin descuento
        
        // Obtener el método de pago si está disponible
        metodoPago = intent.getStringExtra("metodoPago") ?: "No especificado"

        // Log para depuración
        Log.d("VentanaSeleccionarCupon", "Importe original: $importeOriginal")
        Log.d("VentanaSeleccionarCupon", "Método de pago: $metodoPago")

        // Configurar la vista con los datos
        setupUI()
        setupCuponesCards()
        actualizarImportes()

        // Configurar el botón cancelar
        binding.btnCancelar.setOnClickListener {
            finish() // Volver a la pantalla anterior
        }

        // Configurar el botón aceptar
        binding.btnAceptar.setOnClickListener {
            // Mostrar el mensaje de confirmación con animación
            mostrarMensajeConfirmacion()
            
            // Esperar un momento antes de continuar para que se vea la animación
            Handler(Looper.getMainLooper()).postDelayed({
                // Iniciar VentanaMontoFinal con los datos actualizados
                irAVentanaMontoFinal()
            }, 1200) // 1.2 segundos
        }
    }
    
    private fun irAVentanaMontoFinal() {
        // Crear el intent para iniciar VentanaMontoFinal
        val intent = Intent(this, VentanaMontoFinal::class.java)
        
        // Añadir los datos necesarios
        intent.putExtra("montoTotal", importeOriginal)
        intent.putExtra("montoFinal", importeFinal)
        intent.putExtra("metodoPago", metodoPago)
        
        // Calcular el descuento aplicado
        val montoDescuento = importeOriginal - importeFinal
        intent.putExtra("montoDescuento", montoDescuento)
        
        // Si es un descuento porcentual, enviar el porcentaje
        val descuentoPorcentaje = when (descuentoSeleccionado) {
            TipoDescuento.FIJO_10 -> if (importeOriginal > 0) (montoDescuento / importeOriginal) * 100 else 0.0
            TipoDescuento.PORCENTAJE_20 -> 20.0
            TipoDescuento.NINGUNO -> 0.0
        }
        intent.putExtra("descuentoPorcentaje", descuentoPorcentaje)
        
        // Enviar la descripción del descuento
        intent.putExtra("descripcionDescuento", obtenerDescripcionDescuento())
        
        // Iniciar la actividad
        startActivity(intent)
        finish() // Cerrar esta actividad
    }
    
    private fun mostrarMensajeConfirmacion() {
        // Mostrar el mensaje de confirmación
        binding.mensajeConfirmacion.visibility = View.VISIBLE
        
        // Animación de entrada (escala + alpha)
        val scaleX = ObjectAnimator.ofFloat(binding.mensajeConfirmacion, "scaleX", 0.5f, 1.0f)
        val scaleY = ObjectAnimator.ofFloat(binding.mensajeConfirmacion, "scaleY", 0.5f, 1.0f)
        val alpha = ObjectAnimator.ofFloat(binding.mensajeConfirmacion, "alpha", 0f, 1f)
        
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY, alpha)
        animatorSet.duration = 500
        animatorSet.interpolator = OvershootInterpolator()
        animatorSet.start()
        
        // Establecer el texto del mensaje según el descuento aplicado
        val mensaje = when (descuentoSeleccionado) {
            TipoDescuento.FIJO_10 -> "¡Cupón de 10€ aplicado con éxito!"
            TipoDescuento.PORCENTAJE_20 -> "¡Cupón del 20% aplicado con éxito!"
            TipoDescuento.NINGUNO -> "¡Compra sin descuento!"
        }
        binding.textViewConfirmacion.text = mensaje
    }
    
    private fun obtenerImporteDesdeDiferentesFuentes(): Double {
        // Lista de posibles claves donde podría estar el importe
        val posiblesClaves = listOf(
            "montoTotal", "importeTotal", "total", "precioTotal", "precio"
        )
        
        // Imprimir todos los extras para depuración
        Log.d("VentanaSeleccionarCupon", "Todos los extras disponibles:")
        intent.extras?.let { bundle ->
            for (key in bundle.keySet()) {
                val value = bundle.get(key)
                Log.d("VentanaSeleccionarCupon", "Extra: $key = $value")
            }
        } ?: Log.d("VentanaSeleccionarCupon", "No hay extras en el intent")
        
        // Probar con diferentes claves
        for (clave in posiblesClaves) {
            if (intent.hasExtra(clave)) {
                val valor = intent.getDoubleExtra(clave, -1.0)
                Log.d("VentanaSeleccionarCupon", "Probando clave: $clave = $valor")
                if (valor > 0) {
                    Log.d("VentanaSeleccionarCupon", "Importe encontrado en clave: $clave = $valor")
                    return valor
                }
            } else {
                Log.d("VentanaSeleccionarCupon", "Clave no encontrada: $clave")
            }
        }
        
        // Si no se encuentra en ninguna clave, buscar en los extras como string
        for (clave in posiblesClaves) {
            if (intent.hasExtra(clave)) {
                val valorStr = intent.getStringExtra(clave)
                Log.d("VentanaSeleccionarCupon", "Probando clave como String: $clave = $valorStr")
                try {
                    val valorDouble = valorStr?.toDoubleOrNull()
                    if (valorDouble != null && valorDouble > 0) {
                        Log.d("VentanaSeleccionarCupon", "Importe encontrado como String en clave: $clave = $valorDouble")
                        return valorDouble
                    }
                } catch (e: Exception) {
                    Log.e("VentanaSeleccionarCupon", "Error al convertir $valorStr a Double", e)
                }
            }
        }
        
        // Intentar obtenerlo del Bundle
        intent.extras?.let { bundle ->
            for (key in bundle.keySet()) {
                try {
                    val value = bundle.get(key)
                    Log.d("VentanaSeleccionarCupon", "Probando extra: $key = $value")
                    if (value is Number) {
                        val valueDouble = value.toDouble()
                        if (valueDouble > 0) {
                            Log.d("VentanaSeleccionarCupon", "Importe encontrado en extra numérico: $key = $valueDouble")
                            return valueDouble
                        }
                    } else if (value is String) {
                        val valueDouble = value.toDoubleOrNull()
                        if (valueDouble != null && valueDouble > 0) {
                            Log.d("VentanaSeleccionarCupon", "Importe encontrado en extra String: $key = $valueDouble")
                            return valueDouble
                        }
                    }
                } catch (e: Exception) {
                    Log.e("VentanaSeleccionarCupon", "Error al procesar el extra: $key", e)
                }
            }
        }
        
        // IMPORTANTE: Mostrar un Toast para notificar que no se encontró el importe
        Toast.makeText(
            this, 
            "ADVERTENCIA: No se pudo obtener el importe real. Usando valor de desarrollo (59.99€)", 
            Toast.LENGTH_LONG
        ).show()
        
        // Si llegamos aquí, no se encontró el importe en ningún sitio
        // Por ahora, para probar la interfaz, devolvemos un valor de ejemplo
        // TODO: Eliminar esto en producción o manejar el error de otra forma
        return 59.99
    }
    
    private fun obtenerDescripcionDescuento(): String {
        return when (descuentoSeleccionado) {
            TipoDescuento.FIJO_10 -> "10€ de descuento"
            TipoDescuento.PORCENTAJE_20 -> "20% de descuento"
            TipoDescuento.NINGUNO -> "Sin descuento"
        }
    }

    private fun setupUI() {
        // Configurar textos estáticos
        binding.tituloSeleccionCupon.text = "Selección de Cupones"
        binding.infoSeleccionarCupon.text = "Selecciona el cupón que desee aplicar a su compra"
        
        // Configurar textos de los cupones
        binding.radioDescuento1.text = ""  // El texto se muestra en el TextView dentro del CardView
        binding.radioDescuento2.text = ""  // El texto se muestra en el TextView dentro del CardView
        binding.radioNinguno.text = ""     // El texto se muestra en el TextView dentro del CardView

        // Configurar botones
        binding.btnCancelar.text = "Cancelar"
        binding.btnAceptar.text = "Aplicar Cupón"
        
        // Seleccionar por defecto "Ningún Cupón"
        binding.radioNinguno.isChecked = true
        
        // Ocultar el mensaje de confirmación inicialmente
        binding.mensajeConfirmacion.visibility = View.GONE
    }
    
    private fun setupCuponesCards() {
        // Aplicar animación a las tarjetas al cargar la vista
        animarTarjetas()
        
        // Configurar clics en las tarjetas (además de los RadioButtons)
        binding.cardDescuento1.setOnClickListener { seleccionarCupon(TipoDescuento.FIJO_10) }
        binding.cardDescuento2.setOnClickListener { seleccionarCupon(TipoDescuento.PORCENTAJE_20) }
        binding.cardSinDescuento.setOnClickListener { seleccionarCupon(TipoDescuento.NINGUNO) }
        
        // Configurar radio buttons
        binding.radioDescuento1.setOnClickListener { seleccionarCupon(TipoDescuento.FIJO_10) }
        binding.radioDescuento2.setOnClickListener { seleccionarCupon(TipoDescuento.PORCENTAJE_20) }
        binding.radioNinguno.setOnClickListener { seleccionarCupon(TipoDescuento.NINGUNO) }
        
        // Calcular y mostrar el ahorro para cada cupón
        actualizarAhorroEnCupones()
    }
    
    private fun animarTarjetas() {
        val tarjetas = listOf(binding.cardDescuento1, binding.cardDescuento2, binding.cardSinDescuento)
        
        // Ocultar inicialmente
        tarjetas.forEach { it.alpha = 0f }
        
        // Animar con un retraso secuencial
        tarjetas.forEachIndexed { index, card ->
            card.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(300)
                .setInterpolator(OvershootInterpolator())
                .setStartDelay(100L * index)
                .start()
        }
    }
    
    private fun seleccionarCupon(tipo: TipoDescuento) {
        // Actualizar la selección
        descuentoSeleccionado = tipo
        
        // Actualizar estado de los radio buttons
        binding.radioDescuento1.isChecked = tipo == TipoDescuento.FIJO_10
        binding.radioDescuento2.isChecked = tipo == TipoDescuento.PORCENTAJE_20
        binding.radioNinguno.isChecked = tipo == TipoDescuento.NINGUNO
        
        // Resaltar la tarjeta seleccionada con animación
        resaltarTarjetaSeleccionada(tipo)
        
        // Calcular el nuevo importe final según el descuento
        calcularImporteFinal()
        
        // Actualizar la UI
        actualizarImportes()
    }
    
    private fun resaltarTarjetaSeleccionada(tipo: TipoDescuento) {
        // Obtener la tarjeta seleccionada
        val tarjetaSeleccionada = when (tipo) {
            TipoDescuento.FIJO_10 -> binding.cardDescuento1
            TipoDescuento.PORCENTAJE_20 -> binding.cardDescuento2
            TipoDescuento.NINGUNO -> binding.cardSinDescuento
        }
        
        // Lista de todas las tarjetas
        val todasLasTarjetas = listOf(
            binding.cardDescuento1,
            binding.cardDescuento2,
            binding.cardSinDescuento
        )
        
        // Restablecer todas las tarjetas primero
        todasLasTarjetas.forEach { tarjeta ->
            tarjeta.animate()
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setDuration(200)
                .start()
            tarjeta.cardElevation = 4f
        }
        
        // Resaltar la tarjeta seleccionada
        tarjetaSeleccionada.animate()
            .scaleX(1.03f)
            .scaleY(1.03f)
            .setDuration(200)
            .setInterpolator(OvershootInterpolator())
            .start()
        tarjetaSeleccionada.cardElevation = 8f
    }
    
    private fun calcularImporteFinal() {
        importeFinal = when (descuentoSeleccionado) {
            TipoDescuento.FIJO_10 -> maxOf(0.0, importeOriginal - 10.0)
            TipoDescuento.PORCENTAJE_20 -> importeOriginal * 0.8 // 80% del precio original (20% descuento)
            TipoDescuento.NINGUNO -> importeOriginal
        }
    }
    
    private fun actualizarImportes() {
        val formatoMoneda = NumberFormat.getCurrencyInstance(Locale("es", "ES"))
        
        // Actualizar los textos con los importes
        binding.textViewImporteOriginal.text = "Importe original: ${formatoMoneda.format(importeOriginal)}"
        binding.textViewImporteFinal.text = "Importe final: ${formatoMoneda.format(importeFinal)}"
        
        // Si hay descuento, resaltar la diferencia
        val hayDescuento = importeFinal < importeOriginal
        binding.textViewImporteFinal.setTextColor(
            getColor(if (hayDescuento) android.R.color.holo_green_dark else android.R.color.black)
        )
        
        // Animar el cambio del importe final
        if (hayDescuento) {
            binding.textViewImporteFinal.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .setDuration(200)
                .setInterpolator(OvershootInterpolator())
                .withEndAction {
                    binding.textViewImporteFinal.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(200)
                        .start()
                }
                .start()
        }
    }
    
    private fun actualizarAhorroEnCupones() {
        val formatoMoneda = NumberFormat.getCurrencyInstance(Locale("es", "ES"))
        
        // Calcular y mostrar el ahorro para el descuento fijo
        val ahorroFijo = minOf(10.0, importeOriginal) // No puede ahorrar más que el importe total
        binding.textViewAhorro1.text = "Te ahorras: ${formatoMoneda.format(ahorroFijo)}"
        
        // Calcular y mostrar el ahorro para el descuento porcentual
        val ahorroPorcentaje = importeOriginal * 0.2 // 20% del precio original
        binding.textViewAhorro2.text = "Te ahorras: ${formatoMoneda.format(ahorroPorcentaje)}"
    }
}