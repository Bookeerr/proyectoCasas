package com.example.rentfage.ui.screen

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

// 1. La funcion que se conecta al ViewModel
@Composable
fun HomeScreenVm(
    onHouseClick: (Int) -> Unit,
    onGoLogin: () -> Unit,
    onGoRegister: () -> Unit
) {
    // creamos una variable para poder acceder a casasViewModel
    val vm: CasasViewModel = viewModel()
    // Se observa el estado (la lista de casas)
    val state by vm.uiState.collectAsState()
//prueba
    //prueba
    //  es el que da las órdenes de qué hacer cuando el usuario aprieta ese botón
    HomeScreen(
        casas = state.casas, // sirve para dar orden de mostrar
        onHouseClick = onHouseClick, // tipo de orden para ver detalles de la casa
        onGoLogin = onGoLogin, // tipo de orden para ir a login
        onGoRegister = onGoRegister, // tipo de orden para ir a registro
        onToggleFavorite = { casaId -> vm.toggleFavorite(casaId) } // Se pasa la acción de marcar favorito
    )
}

// 2. La funcion que solo dibuja la pantalla
@Composable
private fun HomeScreen(
    casas: List<Casa>, // estan las listas de las casas
    onHouseClick: (Int) -> Unit, // es una orden, lleva a detalles de las casas
    onGoLogin: () -> Unit, // es una orden, lleva a login
    onGoRegister: () -> Unit, // es una orden, lleva a registro
    onToggleFavorite: (Int) -> Unit // se agrego la orden de poner en favorito las casas
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Propiedades Disponibles",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        // Ahora la lista de items viene del ViewModel
        items(casas) { casa ->
            HouseCard(
                casa = casa,
                onClick = { onHouseClick(casa.id) },
                onToggleFavorite = { onToggleFavorite(casa.id) } // Se pasa la accion a la tarjeta
            )
        }
    }
}

// 3. La tarjeta de la casa, ahora con el botón de favorito
@Composable
private fun HouseCard(
    casa: Casa,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit // se agrego la orden de poner en favorito las casas
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

                // Icono de Favorito
                IconButton( // se agrego el icono de favorito, como boton
                    onClick = onToggleFavorite, // cuando se aprete el boton se va a ejecutar
                    modifier = Modifier.align(Alignment.TopEnd) // se coloca en la esquina
                ) {
                    Icon(
                        imageVector = if (casa.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder, // se cambia el icono si es que la selecciona
                        contentDescription = "Marcar como favorito", // descripcion del icono
                        tint = if (casa.isFavorite) Color.Red else Color.White // se cambia el color del icono
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) { // se agrega un espacio entre la imagen y el texto
                Text(
                    text = casa.price, // texto sobre la casa
                    style = MaterialTheme.typography.headlineSmall, // tipo de letra
                    fontWeight = FontWeight.Bold, // tipo de letra en negrita
                    color = MaterialTheme.colorScheme.primary // color del texto sea el primario de la app
                )
                Spacer(modifier = Modifier.height(4.dp)) // espacio
                Text(
                    text = casa.address, // texto sobre la casa, mas especifico la ubi
                    style = MaterialTheme.typography.titleMedium // tipo de letra
                )
                Spacer(modifier = Modifier.height(8.dp)) // espacio
                Text(
                    text = casa.details, // texto sobre la casa, mas especifico la descripcion
                    style = MaterialTheme.typography.bodyMedium, // tipo de letra
                    color = Color.Gray // color del texto
                )
            }
        }
    }
}
