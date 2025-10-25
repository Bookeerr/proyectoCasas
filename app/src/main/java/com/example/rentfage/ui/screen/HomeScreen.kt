package com.example.rentfage.ui.screen

// --- 1. IMPORTACIONES AÑADIDAS ---
import com.example.rentfage.data.local.storage.UserPreferences // Para usar DataStore
import androidx.compose.material.icons.filled.Person           // Icono de usuario logueado
import androidx.compose.material.icons.filled.PersonOff        // Icono de usuario no logueado
import androidx.compose.runtime.remember                       // Para 'remember { UserPreferences(context) }'
import androidx.compose.ui.platform.LocalContext               // Para obtener el contexto
import androidx.lifecycle.compose.collectAsStateWithLifecycle  // Para observar el Flow de DataStore
import androidx.compose.foundation.layout.Row                  // Para poner el título y el icono juntos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rentfage.data.local.Casa
import com.example.rentfage.ui.viewmodel.CasasViewModel

@Composable
fun HomeScreenVm(
    onHouseClick: (Int) -> Unit,
    onGoLogin: () -> Unit,
    onGoRegister: () -> Unit
) {
    // --- 2. LEER DATOS DESDE DATASTORE ---
    val context = LocalContext.current
    val userPrefrs = remember { UserPreferences(context) }
    // Observamos el valor 'isLoggedIn' en tiempo real.
    val isLoggedIn by userPrefrs.isLoggedIn.collectAsStateWithLifecycle(initialValue = false)

    val vm: CasasViewModel = viewModel()
    val state by vm.uiState.collectAsState()

    HomeScreen(
        casas = state.casas,
        isLoggedIn = isLoggedIn, // <-- 3. Pasamos el nuevo valor a la pantalla de UI
        onHouseClick = onHouseClick,
        onGoLogin = onGoLogin,
        onGoRegister = onGoRegister,
        onToggleFavorite = { casaId -> vm.toggleFavorite(casaId) }
    )
}

@Composable
private fun HomeScreen(
    casas: List<Casa>,
    isLoggedIn: Boolean, // <-- 4. Recibimos el estado de la sesión
    onHouseClick: (Int) -> Unit,
    onGoLogin: () -> Unit,
    onGoRegister: () -> Unit,
    onToggleFavorite: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // --- 5. CAMBIO VISUAL EN LA UI ---
            // Ponemos el título y el nuevo icono en una fila.
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Propiedades Disponibles",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 8.dp).weight(1f) // El texto ocupa el espacio sobrante
                )
                // Este es el icono que cambia según el estado de la sesión.
                Icon(
                    imageVector = if (isLoggedIn) Icons.Default.Person else Icons.Default.PersonOff,
                    contentDescription = if (isLoggedIn) "Usuario Logueado" else "Usuario no Logueado",
                    tint = if(isLoggedIn) MaterialTheme.colorScheme.primary else Color.Gray
                )
            }
        }
        items(casas) { casa ->
            HouseCard(
                casa = casa,
                onClick = { onHouseClick(casa.id) },
                onToggleFavorite = { onToggleFavorite(casa.id) }
            )
        }
    }
}

// La tarjeta HouseCard no necesita cambios
@Composable
private fun HouseCard(
    casa: Casa,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.LightGray)
            ) {
                Text("Imagen de la casa", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = if (casa.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Marcar como favorito",
                        tint = if (casa.isFavorite) Color.Red else Color.White
                    )
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = casa.price,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = casa.address,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = casa.details,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}