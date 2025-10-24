package com.example.rentfage.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.rentfage.data.local.Casa
import com.example.rentfage.data.local.casasDeEjemplo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// La "caja" de datos que observará la UI
data class CasasUiState(
    val casas: List<Casa> = emptyList()
)

// gestiona la lista de casas y los favoritos
class CasasViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CasasUiState(casas = casasDeEjemplo))
    val uiState: StateFlow<CasasUiState> = _uiState.asStateFlow()


    fun toggleFavorite(casaId: Int) { //funcion publica para que cualquier pantalla la vea
        _uiState.update {
            val casasActualizadas = it.casas.map {
                if (it.id == casaId) {
                    // Crea una copia de la casa, cambiando solo el estado de isFavorite
                    it.copy(isFavorite = !it.isFavorite)
                } else {
                    it
                }
            }
            it.copy(casas = casasActualizadas)
        }
    }
}
