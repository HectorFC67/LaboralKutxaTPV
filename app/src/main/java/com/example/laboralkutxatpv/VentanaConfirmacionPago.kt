package com.example.laboralkutxatpv

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintJob
import android.print.PrintManager
import android.view.View
import android.view.animation.AnimationUtils
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.laboralkutxatpv.databinding.ActivityVentanaConfirmacionPagoBinding
import com.example.laboralkutxatpv.databinding.DialogEncuestaInvitacionBinding
import com.example.laboralkutxatpv.model.ProductoVendido
import com.example.laboralkutxatpv.model.Venta
import com.example.laboralkutxatpv.repository.VentaRepository
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.content.Context
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import java.lang.reflect.Method

class VentanaConfirmacionPago : AppCompatActivity() {

    private lateinit var binding: ActivityVentanaConfirmacionPagoBinding
    private var dialogoEncuestaShown = false
    private var valoracionSeleccionada = 0
    private var respuestaVideoconsolaSeleccionada = false
    private var tieneVideoconsola = false
    private var montoTotal: Double = 0.0
    private var metodoPago: String = ""
    private var productosSeleccionados: ArrayList<Producto>? = null
    private var montoDescuento: Double = 0.0
    private var descripcionDescuento: String = "Ninguno"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflamos el layout usando View Binding
        binding = ActivityVentanaConfirmacionPagoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Obtener datos de la compra
        obtenerDatosCompra()
        
        // Registrar la venta en el historial
        registrarVenta()
        
        // Configurar textos e información en la UI
        actualizarUI()

        // Botón "Imprimir Ticket"
        binding.btnImprimirTicket.setOnClickListener {
            mostrarPopUpTicket()
        }

        // Botón "Cancelar"
        binding.btnCancelar.setOnClickListener {
            // Acción al pulsar "Cancelar"
            finish() // Cierra esta ventana y vuelve a la anterior
        }

        // Botón "Continuar"
        binding.btnContinuar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
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

        // Recuperar el descuento (si se pasó)
        descripcionDescuento = intent.getStringExtra("descripcionDescuento") ?: "Ninguno"
        montoDescuento = intent.getDoubleExtra("montoDescuento", 0.0)

        @Suppress("UNCHECKED_CAST")
        productosSeleccionados = intent.getSerializableExtra("productos") as? ArrayList<Producto>
    }

    private fun registrarVenta() {
        try {
            // Crear lista de productos vendidos
            val productosVendidos = productosSeleccionados?.map { producto ->
                ProductoVendido(
                    nombreProducto = producto.nombre,
                    precioUnitario = producto.precio,
                    cantidad = producto.cantidad,
                    subtotal = producto.precio * producto.cantidad
                )
            } ?: emptyList()
            
            // Obtener información del cupón
            val cuponUtilizado = intent.getStringExtra("descripcionDescuento") ?: "Ninguno"
            
            // Crear objeto Venta
            val venta = Venta(
                fecha = Date(),
                montoTotal = montoTotal,
                metodoPago = metodoPago,
                cuponUtilizado = cuponUtilizado,
                productos = productosVendidos
            )
            
            // Registrar la venta en el repositorio
            VentaRepository.getInstance().registrarVenta(venta)
            
            // Mostrar confirmación
            Toast.makeText(this, "Venta registrada con éxito", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            // En caso de error al registrar la venta
            Toast.makeText(this, 
                "Error al registrar la venta: " + e.message, 
                Toast.LENGTH_SHORT).show()
        }
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

    private fun mostrarPopUpTicket() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_imprimir_ticket)
        dialog.setCancelable(true)

        val tvContenidoTicket = dialog.findViewById<TextView>(R.id.tvContenidoTicket)
        val btnCerrarTicket = dialog.findViewById<Button>(R.id.btnCerrarTicket)
        val btnImprimirTicketReal = dialog.findViewById<Button>(R.id.btnImprimirTicketReal)

        // Para formatear en € (o la moneda que corresponda)
        val formatoMoneda = NumberFormat.getCurrencyInstance(Locale("es", "ES"))

        val sb = StringBuilder()
        
        // Añadir fecha y hora actual
        val fechaActual = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        sb.append("FECHA: $fechaActual\n")
        sb.append("MÉTODO DE PAGO: $metodoPago\n\n")
        sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n")
        sb.append("DETALLES DE LA COMPRA:\n\n")

        // Listar los productos
        var subtotal = 0.0
        productosSeleccionados?.let { listaProductos ->
            for (producto in listaProductos) {
                val totalProducto = producto.precio * producto.cantidad
                subtotal += totalProducto
                
                sb.append("${producto.nombre}\n")
                sb.append("   ${producto.cantidad} x ${formatoMoneda.format(producto.precio)} = ${formatoMoneda.format(totalProducto)}\n\n")
            }
        }
        
        sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n")

        // Mostrar descripción del descuento si existe
        if (descripcionDescuento != "Ninguno" && montoDescuento > 0.0) {
            sb.append("SUBTOTAL: ${formatoMoneda.format(subtotal)}\n")
            sb.append("DESCUENTO (${descripcionDescuento}): -${formatoMoneda.format(montoDescuento)}\n\n")
            sb.append("TOTAL: ${formatoMoneda.format(subtotal - montoDescuento)}\n\n")
        } else {
            // En caso de no tener descuento, simplemente muestra el monto total
            sb.append("TOTAL: ${formatoMoneda.format(montoTotal)}\n\n")
        }
        
        sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n")
        sb.append("¡Gracias por su compra!\n")
        sb.append("Laboral Kutxa TPV")

        tvContenidoTicket.text = sb.toString()

        btnCerrarTicket.setOnClickListener {
            dialog.dismiss()
        }
        
        btnImprimirTicketReal.setOnClickListener {
            // Mostrar mensaje de impresión
            Toast.makeText(this, "Imprimiendo ticket...", Toast.LENGTH_SHORT).show()
            
            // Imprimir texto en la impresora térmica PAX A920
            // Para pruebas, sólo imprimimos "patata"
            imprimirEnPaxA920("patata")
        }

        dialog.show()
    }
    
    private fun imprimirEnPaxA920(texto: String) {
        // Forzar la impresión directa en la TPV PAX sin intentar detectar el dispositivo
        imprimirDirectamenteEnPax(texto)
    }
    
    /**
     * Método que imprime directamente en la máquina PAX utilizando diferentes enfoques
     */
    private fun imprimirDirectamenteEnPax(texto: String) {
        logImpresion("Intentando imprimir directamente en PAX A920")
        var exito = false
        
        // Intentar con la clase PrinterManager específica de PAX
        if (!exito) {
            exito = intentarImprimirConClasePrinterManager(texto)
        }
        
        // Si el primer método falló, intentar con Q2Printer específico de PAX
        if (!exito) {
            exito = intentarImprimirConQ2Printer(texto)
        }
        
        // Si los métodos anteriores fallaron, intentar con DALManager
        if (!exito) {
            exito = intentarImprimirConDALManager(texto)
        }
        
        // Si todo lo anterior falló, intentar con el método directo de PosApi
        if (!exito) {
            exito = intentarImprimirConPosApi(texto)
        }
        
        // Si ninguno funcionó, mostramos un mensaje de error
        if (!exito) {
            Toast.makeText(this, "No se pudo imprimir en la TPV PAX", Toast.LENGTH_LONG).show()
            logImpresion("Todos los métodos de impresión fallaron")
        }
    }
    
    private fun intentarImprimirConClasePrinterManager(texto: String): Boolean {
        return try {
            logImpresion("Intentando imprimir con PrinterManager")
            
            // Intentar cargar la clase PrinterManager específica de PAX
            val printerManagerClass = Class.forName("com.pax.posdk.PrinterManager")
            val getInstance = printerManagerClass.getMethod("getInstance", Context::class.java)
            val printerManager = getInstance.invoke(null, applicationContext)
            
            // Inicializar impresora
            val initMethod = printerManagerClass.getMethod("init")
            initMethod.invoke(printerManager)
            
            // Imprimir texto
            val printTextMethod = printerManagerClass.getMethod("printStr", String::class.java)
            printTextMethod.invoke(printerManager, texto)
            
            // Añadir líneas en blanco
            printTextMethod.invoke(printerManager, "\n\n\n")
            
            // Iniciar impresión
            val startMethod = printerManagerClass.getMethod("start")
            startMethod.invoke(printerManager)
            
            Toast.makeText(this, "Impresión enviada (PrinterManager)", Toast.LENGTH_SHORT).show()
            logImpresion("Impresión exitosa con PrinterManager")
            true
            
        } catch (e: Exception) {
            logImpresion("Error con PrinterManager: ${e.message}")
            e.printStackTrace()
            false
        }
    }
    
    private fun intentarImprimirConQ2Printer(texto: String): Boolean {
        return try {
            logImpresion("Intentando imprimir con Q2Printer")
            
            // Intentar cargar la clase Q2Printer específica de PAX
            val q2PrinterClass = Class.forName("com.paxsz.easylink.api.Q2Printer")
            val getInstance = q2PrinterClass.getMethod("getInstance")
            val printer = getInstance.invoke(null)
            
            // Inicializar impresora
            val initMethod = q2PrinterClass.getMethod("init")
            initMethod.invoke(printer)
            
            // Imprimir texto
            val appendTextMethod = q2PrinterClass.getMethod("appendText", String::class.java)
            appendTextMethod.invoke(printer, texto)
            
            // Añadir líneas en blanco
            appendTextMethod.invoke(printer, "\n\n\n")
            
            // Iniciar impresión
            val startPrintMethod = q2PrinterClass.getMethod("startPrint")
            startPrintMethod.invoke(printer)
            
            Toast.makeText(this, "Impresión enviada (Q2Printer)", Toast.LENGTH_SHORT).show()
            logImpresion("Impresión exitosa con Q2Printer")
            true
            
        } catch (e: Exception) {
            logImpresion("Error con Q2Printer: ${e.message}")
            e.printStackTrace()
            false
        }
    }
    
    private fun intentarImprimirConDALManager(texto: String): Boolean {
        return try {
            logImpresion("Intentando imprimir con DALManager")
            
            // Usar la API DAL de PAX
            val dalManagerClass = Class.forName("com.pax.dal.impl.DALManagerImpl")
            val dalInstance = dalManagerClass.getMethod("getInstance").invoke(null)
            
            // Obtener la impresora
            val getPrinter = dalManagerClass.getMethod("getPrinter")
            val printer = getPrinter.invoke(dalInstance)
            
            // Inicializar impresora
            val initMethod = printer.javaClass.getMethod("init")
            initMethod.invoke(printer)
            
            // Imprimir texto
            val printStrMethod = printer.javaClass.getMethod("printStr", String::class.java)
            printStrMethod.invoke(printer, texto)
            
            // Avanzar papel
            val stepMethod = printer.javaClass.getMethod("step", Int::class.java)
            stepMethod.invoke(printer, 50)
            
            // Iniciar impresión
            val startMethod = printer.javaClass.getMethod("start")
            startMethod.invoke(printer)
            
            Toast.makeText(this, "Impresión enviada (DALManager)", Toast.LENGTH_SHORT).show()
            logImpresion("Impresión exitosa con DALManager")
            true
            
        } catch (e: Exception) {
            logImpresion("Error con DALManager: ${e.message}")
            e.printStackTrace()
            false
        }
    }
    
    private fun intentarImprimirConPosApi(texto: String): Boolean {
        return try {
            logImpresion("Intentando imprimir con PosApi")
            
            // Intentar el método más directo usando PosApi
            val posApiClass = Class.forName("com.pax.posapi.PosApi")
            val printerOpen = posApiClass.getMethod("printerOpen")
            printerOpen.invoke(null)
            
            // Imprimir texto
            val printString = posApiClass.getMethod("printString", String::class.java)
            printString.invoke(null, texto)
            
            // Avanzar papel
            val printFeed = posApiClass.getMethod("printerFeed", Int::class.java)
            printFeed.invoke(null, 3) // avanzar 3 líneas
            
            // Cortar papel o comenzar impresión
            val printerStart = posApiClass.getMethod("printerStart")
            printerStart.invoke(null)
            
            // Cerrar impresora
            val printerClose = posApiClass.getMethod("printerClose")
            printerClose.invoke(null)
            
            Toast.makeText(this, "Impresión enviada (PosApi)", Toast.LENGTH_SHORT).show()
            logImpresion("Impresión exitosa con PosApi")
            true
            
        } catch (e: Exception) {
            logImpresion("Error con PosApi: ${e.message}")
            e.printStackTrace()
            try {
                // Intento alternativo: usar método de bajo nivel con comandos directos
                val process = Runtime.getRuntime().exec("echo -e \"$texto\\n\\n\\n\" > /dev/ttyGS0")
                val exitValue = process.waitFor()
                if (exitValue == 0) {
                    Toast.makeText(this, "Impresión enviada (comando directo)", Toast.LENGTH_SHORT).show()
                    logImpresion("Impresión exitosa con comando directo")
                    return true
                }
            } catch (e2: Exception) {
                logImpresion("Error con comando directo: ${e2.message}")
                e2.printStackTrace()
            }
            false
        }
    }
    
    private fun logImpresion(mensaje: String) {
        // En un entorno real, aquí registrarías los eventos de impresión
        // en un archivo de log o en una base de datos
        println("LOG_IMPRESORA: $mensaje")
    }
}