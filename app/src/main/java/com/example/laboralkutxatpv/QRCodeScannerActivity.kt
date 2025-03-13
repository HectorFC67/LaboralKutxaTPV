package com.example.laboralkutxatpv

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class QRCodeScannerActivity : AppCompatActivity() {

    // 1) Definimos el "launcher" con ScanContract
    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents == null) {
            // El usuario canceló el escaneo o no se leyó nada
            finish()
        } else {
            // Se obtuvo un contenido del QR
            val contenidoQR = result.contents
            // Lógica que quieras implementar (ejemplo: verificar si es un cupón)
            // if (contenidoQR == "CUPON20") { ... }

            // Para este ejemplo, vamos a la pantalla "Cupón detectado"
            val intent = Intent(this, CouponDetectedActivity::class.java)
            startActivity(intent)
            finish() // Cierra la pantalla de escaneo
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_code_scanner)

        // 2) Iniciar el escaneo del código QR en cuanto se abra esta Activity
        iniciarEscaneo()
    }

    private fun iniciarEscaneo() {
        // 3) Configuramos nuestras opciones de escaneo:
        val options = ScanOptions().apply {
            // Solo escanea QR, si deseas más formatos usa: setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
            setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            setPrompt("Escanea el código QR")
            setBeepEnabled(true)
            setCameraId(0) // 0 = cámara trasera; 1 = cámara frontal
        }
        // 4) Lanzamos el escáner
        barcodeLauncher.launch(options)
    }
}
