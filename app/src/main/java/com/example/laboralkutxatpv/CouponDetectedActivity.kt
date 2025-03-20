package com.example.laboralkutxatpv

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CouponDetectedActivity : AppCompatActivity() {
    private var montoTotal: Double = 0.0
    private var metodoPago: String = "No especificado"
    private val descuentoPorcentaje: Double = 20.0  // Descuento fijo del 20%

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coupon_detected)

        // 1) Recuperar el texto del QR y otros datos
        val contenidoQR = intent.getStringExtra("contenidoQR") ?: "Contenido no encontrado"
        montoTotal = intent.getDoubleExtra("montoTotal", 0.0)
        metodoPago = intent.getStringExtra("metodoPago") ?: "No especificado"

        // 2) Referencia a TextView y mostrar el contenido
        val textViewCupounDetected = findViewById<TextView>(R.id.textViewCupounDetected)
        textViewCupounDetected.text = "Cupón del 20% detectado con éxito"

        // 3) Botón para ir a VentanaMontoFinal
        val buttonGoToVentanaMontoFinal = findViewById<Button>(R.id.buttonGoToMontoFinal)
        buttonGoToVentanaMontoFinal.setOnClickListener {
            val intent = Intent(this, VentanaMontoFinal::class.java)
            
            // Calcular el importe con descuento
            val montoDescuento = montoTotal * (descuentoPorcentaje / 100)
            val montoFinal = montoTotal - montoDescuento
            
            // Pasar los datos necesarios
            intent.putExtra("montoTotal", montoTotal)
            intent.putExtra("montoFinal", montoFinal)
            intent.putExtra("montoDescuento", montoDescuento)
            intent.putExtra("descuentoPorcentaje", descuentoPorcentaje)
            intent.putExtra("descripcionDescuento", "Cupón descuento 20%")
            intent.putExtra("metodoPago", metodoPago)
            
            // Pasar los productos seleccionados si están disponibles
            if (intent.hasExtra("productos")) {
                intent.putExtra("productos", intent.getSerializableExtra("productos"))
            }
            
            startActivity(intent)
        }
    }
}
