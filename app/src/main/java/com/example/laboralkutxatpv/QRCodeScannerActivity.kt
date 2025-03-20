package com.example.laboralkutxatpv

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class QRCodeScannerActivity : AppCompatActivity() {

    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents == null) {
            // El usuario canceló el escaneo o no se leyó nada
            finish()
        } else {
            // Se obtuvo un contenido del QR
            val contenidoQR = result.contents

            // IMPORTANTE: Pasamos el contenidoQR a la siguiente Activity mediante putExtra
            val intent = Intent(this, VentanaConfirmacionPago::class.java)
            intent.putExtra("contenidoQR", contenidoQR)
            
            // Pasar el monto total y método de pago si están disponibles
            if (getIntent().extras != null) {
                val montoTotal = getIntent().getDoubleExtra("montoTotal", 0.0)
                val metodoPago = getIntent().getStringExtra("metodoPago") ?: "No especificado"
                
                intent.putExtra("montoTotal", montoTotal)
                intent.putExtra("metodoPago", metodoPago)
                
                // Pasar los productos seleccionados si están disponibles
                if (getIntent().hasExtra("productos")) {
                    intent.putExtra("productos", getIntent().getSerializableExtra("productos"))
                }
            }
            
            startActivity(intent)

            // Cierra la pantalla de escaneo
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_code_scanner)

        // Iniciar el escaneo del código QR al abrir esta Activity
        iniciarEscaneo()
    }

    private fun iniciarEscaneo() {
        val options = ScanOptions().apply {
            setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            setPrompt("Escanea el código QR")
            setBeepEnabled(true)
            setCameraId(0)
        }
        barcodeLauncher.launch(options)
    }
}
