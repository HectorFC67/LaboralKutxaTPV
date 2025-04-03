package com.example.laboralkutxatpv

import android.content.Context
import android.content.SharedPreferences

/**
 * Clase para gestionar la sesión del usuario (login, logout)
 */
class SessionManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    companion object {
        private const val PREF_NAME = "LaboralKutxaSession"
        private const val IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USERNAME = "username"
    }

    /**
     * Guarda los datos de sesión cuando el usuario inicia sesión
     */
    fun createLoginSession(username: String) {
        editor.putBoolean(IS_LOGGED_IN, true)
        editor.putString(KEY_USERNAME, username)
        editor.apply()
    }

    /**
     * Verifica si el usuario ya está logueado
     */
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false)
    }

    /**
     * Obtiene el nombre de usuario actual
     */
    fun getUsername(): String? {
        return sharedPreferences.getString(KEY_USERNAME, null)
    }

    /**
     * Actualiza el nombre de usuario manteniendo la sesión activa
     */
    fun updateUsername(newUsername: String) {
        editor.putString(KEY_USERNAME, newUsername)
        editor.apply()
    }

    /**
     * Cierra la sesión del usuario eliminando los datos de sesión
     */
    fun logout() {
        editor.clear()
        editor.apply()
    }
} 