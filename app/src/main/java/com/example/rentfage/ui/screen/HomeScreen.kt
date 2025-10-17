package com.example.rentfage.ui.screen

import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// 1. "Molde" temporal para una casa
private data class House(
    val id: Int,
    val price: String,
    val address: String,
    val details: String
)

// 2. Lista de casas de ejemplo (solo en Santiago) con precios en UF
private val sampleHouses = listOf(
    House(1, "UF 28.500", "Av. Vitacura, Vitacura, Santiago", "4 hab | 5 baños | 450 m²"),
    House(2, "UF 35.000", "Camino La Dehesa, Lo Barnechea, Santiago", "5 hab | 6 baños | 600 m² | Piscina"),
    House(3, "UF 26.500", "San Damián, Las Condes, Santiago", "4 hab | 4 baños | 500 m² | Jardín amplio"),
    House(4, "UF 18.000", "Isidora Goyenechea, Las Condes, Santiago", "3 hab | 3 baños | 220 m² | Penthouse")
)

@Composable
fun HomeScreen(
    onGoLogin: () -> Unit,
    onGoRegister: () -> Unit
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
        items(sampleHouses) { house ->
            HouseCard(house = house)
        }
    }
}

// 3. Tarjeta para mostrar una casa individual
@Composable
private fun HouseCard(house: House) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            // Marcador de posición para la imagen de la casa
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.LightGray)
            ) {
                Text("Imagen de la casa", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
            }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = house.price,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = house.address,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = house.details,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}
