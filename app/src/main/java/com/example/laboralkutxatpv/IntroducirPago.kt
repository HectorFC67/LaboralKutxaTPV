package com.example.laboralkutxatpv

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class IntroducirPago : AppCompatActivity() {

    private lateinit var amountTextView: TextView
    private var currentInput: StringBuilder = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagar_directo) // Ensure this matches your XML filename

        // Bind UI elements
        amountTextView = findViewById(R.id.amountTextView)

        // Set up button click listeners
        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        val buttonIds = listOf(
            R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5,
            R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btn10
        )

        // Set numeric button listeners
        for (id in buttonIds) {
            findViewById<Button>(id).setOnClickListener { appendNumber((it as Button).text.toString()) }
        }

        // Special button listeners
        findViewById<Button>(R.id.btnCharge ).setOnClickListener { addDecimal() }
        findViewById<Button>(R.id.btnComma).setOnClickListener { clearAmount() }
        findViewById<Button>(R.id.btnClear).setOnClickListener { processPayment() }
    }

    private fun appendNumber(number: String) {
        if (currentInput.length < 10) { // Limit input length
            currentInput.append(number)
            updateAmountTextView()
        }
    }

    private fun addDecimal() {
        if (!currentInput.contains(",")) { // Prevent multiple decimals
            if (currentInput.isEmpty()) {
                currentInput.append("0,") // Handle case when user starts with ","
            } else {
                currentInput.append(",")
            }
            updateAmountTextView()
        }
    }

    private fun clearAmount() {
        currentInput.clear()
        updateAmountTextView()
    }

    private fun processPayment() {
        if (TextUtils.isEmpty(currentInput.toString())) {
            Toast.makeText(this, "Ingrese un monto antes de pagar", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Procesando pago de $currentInput", Toast.LENGTH_SHORT).show()
            // Replace with actual payment logic
        }
    }

    private fun updateAmountTextView() {
        amountTextView.text = if (currentInput.isEmpty()) "0" else currentInput.toString()
    }
}
