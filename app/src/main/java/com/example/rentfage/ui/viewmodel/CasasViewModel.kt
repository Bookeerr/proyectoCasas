package com.example.rentfage.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.rentfage.R
import com.example.rentfage.data.local.Casa
import com.example.rentfage.data.local.casasDeEjemplo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class CasasUiState(
    val casas: List<Casa> = emptyList()
)

// Novedad: Vuelve a ser un ViewModel simple.
class CasasViewModel : ViewModel() {

    // Novedad: Vuelve a usar la lista de ejemplo directamente.
    private val _uiState = MutableStateFlow(CasasUiState(casas = casasDeEjemplo))
    val uiState: StateFlow<CasasUiState> = _uiState.asStateFlow()


    fun toggleFavorite(casaId: Int) {
        _uiState.update {
            val casasActualizadas = it.casas.map {
                if (it.id == casaId) {
                    it.copy(isFavorite = !it.isFavorite)
                } else {
                    it
                }
            }
            it.copy(casas = casasActualizadas)
        }
    }

    fun deleteCasa(casaId: Int) {
        _uiState.update {
            val casasActualizadas = it.casas.filter { casa -> casa.id != casaId }
            it.copy(casas = casasActualizadas)
        }
    }

    // Novedad: La función ya no pide la imagen.
    fun addCasa(address: String, price: String, details: String, latitude: Double, longitude: Double) {
        _uiState.update {
            val newId = (it.casas.maxOfOrNull { c -> c.id } ?: 0) + 1
            val newCasa = Casa(
                id = newId,
                address = address,
                price = price,
                details = details,
                latitude = latitude,
                longitude = longitude,
                imageResId = R.drawable.casa1, // Asigna una imagen por defecto.
                isFavorite = false
            )
            val casasActualizadas = it.casas + newCasa
            it.copy(casas = casasActualizadas)
        }
    }

    // Novedad: La función ya no pide la imagen.
    fun updateCasa(casaId: Int, address: String, price: String, details: String, latitude: Double, longitude: Double) {
        _uiState.update {
            val casasActualizadas = it.casas.map {
                if (it.id == casaId) {
                    it.copy(
                        address = address,
                        price = price,
                        details = details,
                        latitude = latitude,
                        longitude = longitude
                    )
                } else {
                    it
                }
            }
            it.copy(casas = casasActualizadas)
        }
    }
}
