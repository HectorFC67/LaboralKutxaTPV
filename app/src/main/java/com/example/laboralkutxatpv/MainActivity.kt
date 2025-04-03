package com.example.laboralkutxatpv

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.laboralkutxatpv.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import android.view.inputmethod.InputMethodManager
import android.content.Context
import android.view.MotionEvent
import android.widget.LinearLayout
import android.graphics.Rect

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar SessionManager
        sessionManager = SessionManager(this)

        // Verificar si el usuario está logueado
        if (!sessionManager.isLoggedIn()) {
            // Si no está logueado, redirigir a la pantalla de login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        
        // Quitar el título de la barra de herramientas
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Configurar Navigation Drawer
        drawerLayout = binding.drawerLayout
        val navigationView = binding.navView
        
        // Configurar ActionBarDrawerToggle (hamburger icon)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        
        // Añadir listener para guardar cuando se cierra el drawer
        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerOpened(drawerView: View) {}
            override fun onDrawerStateChanged(newState: Int) {}
            
            override fun onDrawerClosed(drawerView: View) {
                // Verificar si estamos en modo edición y guardar
                val usernameEditText = drawerView.findViewById<EditText>(R.id.nav_header_username)
                val initialTextView = drawerView.findViewById<TextView>(R.id.initial_text)
                
                if (usernameEditText?.isEnabled == true) {
                    saveUsername(usernameEditText, initialTextView)
                }
            }
        })
        
        navigationView.setNavigationItemSelectedListener(this)
        
        // Configurar evento de toque global para guardar al tocar en cualquier parte de la interfaz
        setupTouchListenerToSaveOnTouch()
        
        // Configurar el nombre de usuario en el header del drawer
        val headerView = navigationView.getHeaderView(0)
        val usernameEditText = headerView.findViewById<EditText>(R.id.nav_header_username)
        val username = sessionManager.getUsername() ?: "Usuario"
        usernameEditText.setText(username)
        
        // Configurar la inicial en el círculo
        val initialTextView = headerView.findViewById<TextView>(R.id.initial_text)
        val userInitial = username.firstOrNull()?.toString()?.uppercase() ?: "U"
        initialTextView.text = userInitial
        
        // Guardar automáticamente cuando se pierde el foco
        usernameEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && usernameEditText.isEnabled) {
                saveUsername(usernameEditText, initialTextView)
            }
        }
        
        // Establecer un touch listener en el layout raíz para detectar clics fuera del EditText
        val headerRootView = headerView.findViewById<View>(R.id.nav_header_root)
        headerRootView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN && usernameEditText.isEnabled) {
                // Calcular si el toque está dentro del EditText
                val editTextRect = Rect()
                usernameEditText.getGlobalVisibleRect(editTextRect)
                
                // Si el toque está fuera del EditText, quitamos el foco
                if (!editTextRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    // Esconder el teclado
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(usernameEditText.windowToken, 0)
                    
                    // Darle foco al contenedor para quitárselo al EditText
                    headerRootView.requestFocus()
                    
                    // Guardar el texto
                    saveUsername(usernameEditText, initialTextView)
                    
                    // Consumir el evento
                    return@setOnTouchListener true
                }
            }
            // No consumir el evento si no estamos en edición o si el toque fue en el EditText
            return@setOnTouchListener false
        }
        
        // Añadir un touch listener para toda la barra lateral
        navigationView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN && usernameEditText.isEnabled) {
                // Calcular si el toque está dentro del EditText
                val editTextRect = Rect()
                usernameEditText.getGlobalVisibleRect(editTextRect)
                
                // Si el toque está fuera del EditText, quitamos el foco
                if (!editTextRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    // Esconder el teclado
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(usernameEditText.windowToken, 0)
                    
                    // Guardar el texto
                    saveUsername(usernameEditText, initialTextView)
                    
                    // No consumimos el evento para que pueda propagarse a los elementos hijos (menú)
                    return@setOnTouchListener false
                }
            }
            // No consumir el evento
            return@setOnTouchListener false
        }

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            navController.graph, drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    private fun setupTouchListenerToSaveOnTouch() {
        // Configurar un touch listener global para capturar toques en toda la ventana
        val rootView = window.decorView.findViewById<View>(android.R.id.content)
        rootView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                // Si el drawer está abierto y estamos editando el nombre
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    val headerView = binding.navView.getHeaderView(0)
                    val usernameEditText = headerView.findViewById<EditText>(R.id.nav_header_username)
                    val initialTextView = headerView.findViewById<TextView>(R.id.initial_text)
                    
                    if (usernameEditText.isEnabled) {
                        // Calcular si el toque está dentro del EditText
                        val editTextRect = Rect()
                        usernameEditText.getGlobalVisibleRect(editTextRect)
                        
                        // Si el toque está fuera del EditText, guardar y quitar el foco
                        if (!editTextRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                            // Esconder el teclado
                            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(usernameEditText.windowToken, 0)
                            
                            // Guardar el nombre
                            saveUsername(usernameEditText, initialTextView)
                        }
                    }
                }
            }
            // Nunca consumimos el evento, solo lo observamos
            return@setOnTouchListener false
        }
    }

    override fun onBackPressed() {
        // Verificar si hay que guardar el nombre de usuario
        val headerView = binding.navView.getHeaderView(0)
        val usernameEditText = headerView.findViewById<EditText>(R.id.nav_header_username)
        val initialTextView = headerView.findViewById<TextView>(R.id.initial_text)
        
        if (usernameEditText.isEnabled) {
            saveUsername(usernameEditText, initialTextView)
            return
        }
        
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Guardar el nombre de usuario si está en modo edición
        val headerView = binding.navView.getHeaderView(0)
        val usernameEditText = headerView.findViewById<EditText>(R.id.nav_header_username)
        val initialTextView = headerView.findViewById<TextView>(R.id.initial_text)
        
        if (usernameEditText.isEnabled) {
            saveUsername(usernameEditText, initialTextView)
        }
        
        // Proceder con la acción del menú seleccionado
        when (item.itemId) {
            R.id.nav_edit_profile -> {
                // Activar la edición del nombre de usuario
                // Activar la edición
                usernameEditText.isEnabled = true
                usernameEditText.requestFocus()
                
                // Seleccionar todo el texto para mejor indicación visual
                usernameEditText.selectAll()
                
                // Mostrar el teclado
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(usernameEditText, InputMethodManager.SHOW_IMPLICIT)
                
                // No cerramos el drawer para que el usuario pueda editar
                return true
            }
            R.id.nav_logout -> {
                // Cerrar sesión
                sessionManager.logout()
                
                // Redirigir a la pantalla de login
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
        
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    // Función para guardar el nombre de usuario
    private fun saveUsername(usernameEditText: EditText, initialTextView: TextView) {
        // Guardar el nombre de usuario
        val newUsername = usernameEditText.text.toString().trim()
        if (newUsername.isNotEmpty()) {
            // Actualizar el nombre en SharedPreferences
            sessionManager.updateUsername(newUsername)
            
            // Actualizar la inicial
            val newInitial = newUsername.firstOrNull()?.toString()?.uppercase() ?: "U"
            initialTextView.text = newInitial
            
            // Desactivar la edición
            usernameEditText.isEnabled = false
            
            // Mostrar mensaje de éxito
            Toast.makeText(this, "Nombre actualizado", Toast.LENGTH_SHORT).show()
        } else {
            // Si está vacío, restaurar el nombre original
            val originalUsername = sessionManager.getUsername() ?: "Usuario"
            usernameEditText.setText(originalUsername)
            usernameEditText.isEnabled = false
            Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
        }
    }
}