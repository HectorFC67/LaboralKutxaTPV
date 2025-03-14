package com.example.laboralkutxatpv

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.laboralkutxatpv.databinding.ActivityVentanaConfirmacionPagoBinding
import com.example.laboralkutxatpv.databinding.DialogEncuestaInvitacionBinding
import java.text.NumberFormat
import java.util.Locale

class VentanaConfirmacionPago : AppCompatActivity() {

    private lateinit var binding: ActivityVentanaConfirmacionPagoBinding
    private var dialogoEncuestaShown = false
    private var valoracionSeleccionada = 0
    private var respuestaVideoconsolaSeleccionada = false
    private var tieneVideoconsola = false
    private var montoTotal: Double = 0.0
    private var metodoPago: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflamos el layout usando View Binding
        binding = ActivityVentanaConfirmacionPagoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Obtener datos de la compra
        obtenerDatosCompra()
        
        // Configurar textos e información en la UI
        actualizarUI()

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

        // Mostrar el diálogo de encuesta después de 0.5 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            if (!isFinishing && !dialogoEncuestaShown) {
                mostrarDialogoEncuesta()
                dialogoEncuestaShown = true
            }
        }, 500) // 500 ms = 0.5 segundos
    }
    
    private fun obtenerDatosCompra() {
        // Recuperar monto total y método de pago
        montoTotal = intent.getDoubleExtra("montoTotal", 0.0)
        metodoPago = intent.getStringExtra("metodoPago") ?: "No especificado"
    }
    
    private fun actualizarUI() {
        // Formatear el monto para mostrarlo
        val formatoMoneda = NumberFormat.getCurrencyInstance(Locale("es", "ES"))
        val montoFormateado = formatoMoneda.format(montoTotal)
        
        // Configurar texto para el pago exitoso
        binding.tvPagoExito.text = "Pago realizado con éxito"
        
        // Configurar el mensaje de progreso hacia el próximo cupón
        // Se necesitan 100€ para ganar un cupón
        val montoParaCupon = 100.0
        
        // Lógica simplificada: solo se puede obtener 1 cupón como máximo
        if (montoTotal >= montoParaCupon) {
            // Ha conseguido el cupón
            binding.tvFaltan.text = "¡Has ganado un cupón!"
            
            // Barra de progreso completa
            binding.progressBarCupon.progress = 100
            
            // Cambiar color a morado y animar
            val typedValue = android.util.TypedValue()
            theme.resolveAttribute(android.R.attr.colorPrimary, typedValue, true)
            binding.progressBarCupon.progressTintList = android.content.res.ColorStateList.valueOf(typedValue.data)
            
            // Animar para destacar
            val animacion = android.view.animation.AlphaAnimation(0.5f, 1.0f)
            animacion.duration = 500
            animacion.repeatMode = android.view.animation.Animation.REVERSE
            animacion.repeatCount = 3
            binding.progressBarCupon.startAnimation(animacion)
        } else {
            // No ha conseguido el cupón
            val montoFaltante = montoParaCupon - montoTotal
            binding.tvFaltan.text = "Te faltan ${formatoMoneda.format(montoFaltante)} para recibir un cupón"
            
            // Actualizar la barra de progreso
            val progreso = ((montoTotal / montoParaCupon) * 100).toInt()
            binding.progressBarCupon.progress = progreso
            
            // Color verde para el progreso
            binding.progressBarCupon.progressTintList = android.content.res.ColorStateList.valueOf(getColor(android.R.color.holo_green_dark))
        }
        
        // Configurar los textos de los botones
        binding.btnCancelar.text = "Cancelar"
        binding.btnContinuar.text = "Continuar"
    }

    private fun mostrarDialogoEncuesta() {
        val dialog = Dialog(this)
        val dialogBinding = DialogEncuestaInvitacionBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        
        // Configurar el diálogo
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
        
        // Lista de estrellas para iterar sobre ellas
        val estrellas = listOf(
            dialogBinding.estrella1,
            dialogBinding.estrella2,
            dialogBinding.estrella3,
            dialogBinding.estrella4,
            dialogBinding.estrella5
        )
        
        // Configurar eventos para cada estrella
        estrellas.forEachIndexed { index, estrella ->
            estrella.setOnClickListener {
                // Actualizar la valoración
                valoracionSeleccionada = index + 1
                
                // Actualizar las estrellas (colorear hasta la seleccionada)
                actualizarEstrellas(estrellas, valoracionSeleccionada)
                
                // Reproducir sonido y animación
                reproducirEfecto(estrella)
            }
        }
        
        // Configurar botones de respuesta sobre videoconsolas
        val btnSi = dialogBinding.btnSiVideoconsola
        val btnNo = dialogBinding.btnNoVideoconsola
        
        btnSi.setOnClickListener {
            seleccionarBotonVideoconsola(btnSi, btnNo)
            tieneVideoconsola = true
            respuestaVideoconsolaSeleccionada = true
            reproducirEfecto(btnSi)
        }
        
        btnNo.setOnClickListener {
            seleccionarBotonVideoconsola(btnNo, btnSi)
            tieneVideoconsola = false
            respuestaVideoconsolaSeleccionada = true
            reproducirEfecto(btnNo)
        }
        
        // Botón "Enviar respuesta"
        dialogBinding.btnEnviarRespuesta.setOnClickListener {
            if (valoracionSeleccionada > 0 && respuestaVideoconsolaSeleccionada) {
                // Preparar el mensaje de videoconsolas
                val mensajeVideoconsola = if (tieneVideoconsola) "Sí tienes videoconsolas" else "No tienes videoconsolas"
                
                // Mostrar mensaje de agradecimiento
                Toast.makeText(
                    this, 
                    "¡Gracias por su valoración de $valoracionSeleccionada estrellas! $mensajeVideoconsola",
                    Toast.LENGTH_SHORT
                ).show()
                
                // Cerrar el diálogo
                dialog.dismiss()
            } else {
                // Mensajes de validación
                if (valoracionSeleccionada == 0) {
                    Toast.makeText(
                        this,
                        "Por favor, seleccione una valoración",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (!respuestaVideoconsolaSeleccionada) {
                    Toast.makeText(
                        this,
                        "Por favor, responda si tiene videoconsolas",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        
        dialog.show()
    }
    
    private fun seleccionarBotonVideoconsola(botonSeleccionado: Button, botonNoSeleccionado: Button) {
        // Estilo para el botón seleccionado
        val typedValue = android.util.TypedValue()
        theme.resolveAttribute(android.R.attr.colorPrimary, typedValue, true)
        botonSeleccionado.setBackgroundColor(typedValue.data)
        botonSeleccionado.setTextColor(getColor(android.R.color.white))
        
        // Estilo para el botón no seleccionado
        botonNoSeleccionado.setBackgroundColor(getColor(android.R.color.darker_gray))
        botonNoSeleccionado.setTextColor(getColor(android.R.color.black))
    }
    
    private fun actualizarEstrellas(estrellas: List<ImageView>, valoracion: Int) {
        estrellas.forEachIndexed { index, estrella ->
            if (index < valoracion) {
                // Colorear estrella (seleccionada)
                estrella.setImageResource(android.R.drawable.btn_star_big_on)
            } else {
                // Estrella sin colorear
                estrella.setImageResource(android.R.drawable.btn_star_big_off)
            }
        }
    }
    
    private fun reproducirEfecto(vista: View) {
        // Efecto visual
        val animacion = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        vista.startAnimation(animacion)
        
        // Efecto sonoro
        try {
            val mediaPlayer = MediaPlayer.create(this, R.raw.pop6)
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener { mp -> mp.release() }
        } catch (e: Exception) {
            // Si hay algún error con el sonido, simplemente lo ignoramos
        }
    }
}
