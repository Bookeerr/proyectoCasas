package com.example.rentfage.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// La "caja" de datos para la UI del perfil
data class PerfilUiState(
    val name: String = "Usuario no encontrado", // Valor por defecto
    val email: String = ""
)

// El "cerebro" de la pantalla de perfil
class PerfilViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    init {
        // En cuanto se crea el ViewModel, busca los datos del usuario activo.
        cargarDatosUsuario()
    }

    private fun cargarDatosUsuario() {
        // 1. Lee el email del AuthViewModel.
        val emailUsuarioActivo = AuthViewModel.activeUserEmail

        if (emailUsuarioActivo != null) {
            // 2. Busca al usuario en la lista "pública" del AuthViewModel.
            val usuarioEncontrado = AuthViewModel.USERS.find { it.email.equals(emailUsuarioActivo, ignoreCase = true) }

            if (usuarioEncontrado != null) {
                // 3. Si lo encuentra, actualiza el estado con los datos reales.
                _uiState.value = PerfilUiState(
                    name = usuarioEncontrado.name,
                    email = usuarioEncontrado.email
                )
            }
        }
        // Si no encuentra el email o el usuario, se quedará con los valores por defecto "Usuario no encontrado".
    }
}
