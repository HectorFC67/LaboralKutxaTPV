package com.example.laboralkutxatpv

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.LinearLayout
import androidx.core.content.ContextCompat

class LoginActivity : AppCompatActivity() {
    private lateinit var logoImage: ImageView
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var forgotPasswordText: TextView
    private lateinit var googleLoginButton: Button
    private lateinit var signUpText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(createLoginView())
    }

    private fun createLoginView(): View {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
            setBackgroundColor(Color.WHITE)
        }

        logoImage = ImageView(this).apply {
            setImageDrawable(ContextCompat.getDrawable(this@LoginActivity, R.drawable.logo_laboral))
            layoutParams = LinearLayout.LayoutParams(200, 200).apply {
                gravity = android.view.Gravity.CENTER
                setMargins(0, 50, 0, 30)
            }
        }

        usernameEditText = EditText(this).apply {
            hint = "Phone number, username, or email"
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        passwordEditText = EditText(this).apply {
            hint = "Password"
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        loginButton = Button(this).apply {
            text = "Log in"
            setBackgroundColor(Color.BLUE)
            setTextColor(Color.WHITE)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setOnClickListener {
                val username = usernameEditText.text.toString()
                val password = passwordEditText.text.toString()
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@LoginActivity, "Please enter valid credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }

        forgotPasswordText = TextView(this).apply {
            text = "Forgot password?"
            setTextColor(Color.BLUE)
            setOnClickListener {
                Toast.makeText(this@LoginActivity, "Forgot Password clicked", Toast.LENGTH_SHORT).show()
            }
        }

        googleLoginButton = Button(this).apply {
            text = "Log in with Google"
            setBackgroundColor(Color.WHITE)
            setTextColor(Color.BLACK)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setOnClickListener {
                Toast.makeText(this@LoginActivity, "Google Login clicked", Toast.LENGTH_SHORT).show()
            }
        }

        signUpText = TextView(this).apply {
            text = "Don't have an account? Sign up"
            setTextColor(Color.BLUE)
            setOnClickListener {
                val intent = Intent(this@LoginActivity, SecondFragment::class.java)
                startActivity(intent)
            }
        }

        layout.addView(logoImage)
        layout.addView(usernameEditText)
        layout.addView(passwordEditText)
        layout.addView(loginButton)
        layout.addView(forgotPasswordText)
        layout.addView(googleLoginButton)
        layout.addView(signUpText)

        return layout
    }
}
