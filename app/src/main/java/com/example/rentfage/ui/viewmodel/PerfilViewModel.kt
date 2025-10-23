package com.example.rentfage.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


data class PerfilUiState(
    val name: String = "Usuario no encontrado",
    val email: String = "",
    val phone: String = "", // Campo para el teléfono
    val initials: String = "--"
)

class PerfilViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    init {
        cargarDatosUsuario()
    }

    private fun cargarDatosUsuario() {
        val emailUsuarioActivo = AuthViewModel.activeUserEmail

        if (emailUsuarioActivo != null) {
            val usuarioEncontrado = AuthViewModel.USERS.find { it.email.equals(emailUsuarioActivo, ignoreCase = true) }

            if (usuarioEncontrado != null) {
                _uiState.value = PerfilUiState(
                    name = usuarioEncontrado.name,
                    email = usuarioEncontrado.email,
                    phone = usuarioEncontrado.phone, // Se obtiene el teléfono del usuario
                    initials = getInitials(usuarioEncontrado.name)
                )
            }
        }
    }

    private fun getInitials(name: String): String {
        return name.split(' ')
            .filter { it.isNotBlank() }
            .take(2)
            .map { it.first().uppercase() }
            .joinToString("")
    }
}
