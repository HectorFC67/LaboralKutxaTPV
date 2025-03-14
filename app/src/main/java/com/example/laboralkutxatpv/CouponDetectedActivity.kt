package com.example.laboralkutxatpv

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CouponDetectedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coupon_detected)

        // 1) Recuperar el texto del QR
        val contenidoQR = intent.getStringExtra("contenidoQR") ?: "Contenido no encontrado"

        // 2) Referencia a TextView y mostrar el contenido
        val textViewCupounDetected = findViewById<TextView>(R.id.textViewCupounDetected)
        textViewCupounDetected.text = "$contenidoQR detectado con éxito"

        // 3) Botón para ir a VentanaMontoFinal
        val buttonGoToVentanaMontoFinal = findViewById<Button>(R.id.buttonGoToMontoFinal)
        buttonGoToVentanaMontoFinal.setOnClickListener {
            val intent = Intent(this, VentanaMontoFinal::class.java)
            startActivity(intent)
        }
    }
}
