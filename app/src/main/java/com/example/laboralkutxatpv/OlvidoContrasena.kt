package com.example.laboralkutxatpv

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class OlvidoContrasena : AppCompatActivity() {

    private lateinit var verificationCodeInput: EditText
    private lateinit var newPasswordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var resetPasswordButton: Button
    private lateinit var titleTextView: TextView
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        // Bind views to XML components
        titleTextView = findViewById(R.id.tv_title)
        verificationCodeInput = findViewById(R.id.et_verification_code)
        newPasswordInput = findViewById(R.id.et_new_password)
        confirmPasswordInput = findViewById(R.id.et_confirm_password)
        resetPasswordButton = findViewById(R.id.btn_reset_password)

        // Initialize progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Resetting Password...")

        // Set click listener for password reset button
        resetPasswordButton.setOnClickListener {
            resetPassword()
        }
    }

    private fun resetPassword() {
        val verificationCode = verificationCodeInput.text.toString().trim()
        val newPassword = newPasswordInput.text.toString().trim()
        val confirmPassword = confirmPasswordInput.text.toString().trim()

        if (TextUtils.isEmpty(verificationCode) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        if (newPassword != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        if (newPassword.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show()
            return
        }

        // Show progress dialog
        progressDialog.show()

        // Simulating an API call delay (replace with actual backend call)
        android.os.Handler().postDelayed({
            progressDialog.dismiss()
            Toast.makeText(this, "Password reset successful!", Toast.LENGTH_LONG).show()

            // Redirect user to login screen after resetting password
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 2000)
    }
}
