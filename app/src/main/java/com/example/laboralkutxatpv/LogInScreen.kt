package com.example.laboralkutxatpv

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.laboralkutxatpv.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar el SessionManager
        sessionManager = SessionManager(this)

        // Si el usuario ya está logueado, ir directamente a MainActivity
        if (sessionManager.isLoggedIn()) {
            navigateToMainActivity()
            return
        }

        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            
            if (username.isNotEmpty() && password.isNotEmpty()) {
                // Guardar datos de sesión
                sessionManager.createLoginSession(username)
                
                // Navegar a MainActivity
                navigateToMainActivity()
                
                Toast.makeText(this, "Bienvenido, $username", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Por favor, ingrese credenciales válidas", Toast.LENGTH_SHORT).show()
            }
        }

        binding.forgotPasswordTextView.setOnClickListener {

            navigateToContrasenaOlvidada()
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun navigateToContrasenaOlvidada() {
        val intent = Intent(this, ContrasenaOlvidada::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
