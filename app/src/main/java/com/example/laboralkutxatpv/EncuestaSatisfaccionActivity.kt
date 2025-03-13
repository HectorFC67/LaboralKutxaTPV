package com.example.laboralkutxatpv

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.laboralkutxatpv.databinding.ActivityEncuestaSatisfaccionBinding

class EncuestaSatisfaccionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEncuestaSatisfaccionBinding
    
    // Variables para saber qué botones están seleccionados
    private var pregunta1Seleccionada = false
    private var pregunta2Seleccionada = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityEncuestaSatisfaccionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Configurar botones para la primera pregunta
        binding.btnSiPregunta1.setOnClickListener {
            seleccionarBoton(binding.btnSiPregunta1, binding.btnNoPregunta1)
            pregunta1Seleccionada = true
            verificarRespuestas()
        }
        
        binding.btnNoPregunta1.setOnClickListener {
            seleccionarBoton(binding.btnNoPregunta1, binding.btnSiPregunta1)
            pregunta1Seleccionada = true
            verificarRespuestas()
        }
        
        // Configurar botones para la segunda pregunta
        binding.btnSiPregunta2.setOnClickListener {
            seleccionarBoton(binding.btnSiPregunta2, binding.btnNoPregunta2)
            pregunta2Seleccionada = true
            verificarRespuestas()
        }
        
        binding.btnNoPregunta2.setOnClickListener {
            seleccionarBoton(binding.btnNoPregunta2, binding.btnSiPregunta2)
            pregunta2Seleccionada = true
            verificarRespuestas()
        }
        
        // Configurar botón de finalizar encuesta
        binding.btnFinalizarEncuesta.setOnClickListener {
            // Aquí podrías guardar las respuestas o mostrar un mensaje de agradecimiento
            finish()
        }
    }
    
    // Función para seleccionar un botón y deseleccionar el otro
    private fun seleccionarBoton(botonSeleccionado: Button, botonNoSeleccionado: Button) {
        // Aplicar estilo seleccionado al botón que se ha presionado
        val colorPrimario = obtenerColorPrimario()
        botonSeleccionado.setBackgroundColor(colorPrimario)
        botonSeleccionado.setTextColor(getColor(android.R.color.white))
        
        // Aplicar estilo no seleccionado al otro botón
        botonNoSeleccionado.setBackgroundColor(getColor(android.R.color.darker_gray))
        botonNoSeleccionado.setTextColor(getColor(android.R.color.black))
    }
    
    // Obtener el color primario del tema actual
    private fun obtenerColorPrimario(): Int {
        val typedValue = android.util.TypedValue()
        theme.resolveAttribute(android.R.attr.colorPrimary, typedValue, true)
        return typedValue.data
    }
    
    // Verificar si ambas preguntas tienen respuesta para activar el botón de finalizar
    private fun verificarRespuestas() {
        binding.btnFinalizarEncuesta.isEnabled = pregunta1Seleccionada && pregunta2Seleccionada
    }
} 