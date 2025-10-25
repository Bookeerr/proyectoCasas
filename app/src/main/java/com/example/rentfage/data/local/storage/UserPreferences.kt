package com.example.rentfage.data.local.storage

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión para crear la instancia de DataStore
val Context.dataStore by preferencesDataStore("user_prefs")

class UserPreferences(private val context: Context) {
    // Llave para guardar el valor booleano
    private val isLoggedInKey = booleanPreferencesKey("is_logged_in")

    // Función para escribir el estado de la sesión
    suspend fun setLoggedIn(value: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[isLoggedInKey] = value
        }
    }

    // Flow para leer el estado de la sesión en tiempo real
    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { prefs ->
            // Si el valor no existe, devuelve 'false' por defecto
            prefs[isLoggedInKey] ?: false
        }
}