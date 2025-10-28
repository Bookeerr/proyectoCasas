package com.example.rentfage.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rentfage.data.local.Casa
import com.example.rentfage.ui.viewmodel.CasasViewModel

@Composable
fun AdminPropertyListScreen(
    casasViewModel: CasasViewModel,
    onAddProperty: () -> Unit,      // Novedad: Recibimos la acción para añadir.
    onEditProperty: (Int) -> Unit  // Novedad: Recibimos la acción para editar.
) {
    val casasState by casasViewModel.uiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var propertyToDelete by remember { mutableStateOf<Casa?>(null) }

    Scaffold(
        floatingActionButton = {
            // Novedad: El botón "+" ahora navega al formulario.
            FloatingActionButton(onClick = onAddProperty) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Propiedad")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(text = "Gestionar Propiedades", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(casasState.casas) { casa ->
                PropertyListItem(
                    casa = casa,
                    onDeleteClick = {
                        propertyToDelete = casa
                        showDeleteDialog = true
                    },
                    // Novedad: Pasamos la acción de editar al item de la lista.
                    onEditClick = { onEditProperty(casa.id) }
                )
            }
        }
    }

    if (showDeleteDialog && propertyToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Estás seguro de que quieres eliminar la propiedad en ${propertyToDelete!!.address}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        casasViewModel.deleteCasa(propertyToDelete!!.id)
                        showDeleteDialog = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun PropertyListItem(
    casa: Casa,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit // Novedad: Recibimos la acción para editar.
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = casa.address, style = MaterialTheme.typography.titleMedium)
                Text(text = casa.price, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Row {
                // Novedad: El botón "Editar" ahora navega al formulario con el ID.
                Button(onClick = onEditClick) {
                    Text("Editar")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onDeleteClick) {
                    Text("Eliminar")
                }
            }
        }
    }
}
