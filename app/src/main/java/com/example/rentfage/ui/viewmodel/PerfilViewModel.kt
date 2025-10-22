package com.example.rentfage.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// La "caja" de datos para la UI del perfil
data class PerfilUiState(
    val name: String = "Nombre Apellido", // Dato de ejemplo
    val email: String = "email@ejemplo.com" // Dato de ejemplo
)

// El "cerebro" de la pantalla de perfil
class PerfilViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    // Por ahora, este ViewModel es muy simple.
    // En el futuro, aquí iría la lógica para obtener los datos del usuario real.
}
