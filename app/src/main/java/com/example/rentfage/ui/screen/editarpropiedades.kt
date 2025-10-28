package com.example.rentfage.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.rentfage.ui.viewmodel.CasasViewModel

@Composable
fun AddEditPropertyScreen(
    casasViewModel: CasasViewModel,
    casaId: Int?,
    onNavigateBack: () -> Unit
) {
    val isEditing = casaId != null
    val casasState by casasViewModel.uiState.collectAsState()

    // Estados para cada campo del formulario.
    var address by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }

    // Si estamos editando, cargamos los datos de la casa en los campos.
    LaunchedEffect(casaId) {
        if (isEditing) {
            val casaToEdit = casasState.casas.find { it.id == casaId }
            if (casaToEdit != null) {
                address = casaToEdit.address
                price = casaToEdit.price
                details = casaToEdit.details
                latitude = casaToEdit.latitude.toString()
                longitude = casaToEdit.longitude.toString()
            }
        }
    }

    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize().padding(it).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = if (isEditing) "Editar Propiedad" else "Añadir Nueva Propiedad",
                style = MaterialTheme.typography.headlineMedium // Novedad: Se ha corregido el error de tipeo.
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Precio (ej: UF 28.500)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = details,
                onValueChange = { details = it },
                label = { Text("Detalles (ej: 4 hab | 2 baños | 450 m²)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = latitude,
                onValueChange = { latitude = it },
                label = { Text("Latitud") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = longitude,
                onValueChange = { longitude = it },
                label = { Text("Longitud") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val lat = latitude.toDoubleOrNull() ?: 0.0
                    val lon = longitude.toDoubleOrNull() ?: 0.0

                    if (isEditing) {
                        casasViewModel.updateCasa(casaId!!, address, price, details, lat, lon)
                    } else {
                        casasViewModel.addCasa(address, price, details, lat, lon)
                    }
                    // Después de guardar, volvemos a la pantalla anterior.
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }
        }
    }
}
