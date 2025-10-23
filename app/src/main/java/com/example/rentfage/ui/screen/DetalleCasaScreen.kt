package com.example.rentfage.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.rentfage.data.local.casasDeEjemplo

@Composable
fun DetalleCasaScreen(casaId: Int) {
    // 1. Se busca la casa correcta en la lista.
    val casa = casasDeEjemplo.find { it.id == casaId }

    // 2. Si no se encuentra la casa, se muestra un mensaje.
    if (casa == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Casa no encontrada.")
        }
        return // Se detiene la ejecución aquí.
    }

    // 3. Se construye el boceto de la pantalla.
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        // Marcador para la imagen principal
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(Color.LightGray)
            ) {
                Text("Imagen Principal", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
            }
        }

        // Contenido con los detalles de la casa
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = casa.price,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = casa.address,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Descripción de la propiedad",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Esta magnifica propiedad ubicada en ${casa.address} cuenta con ${casa.details}. Una oportunidad unica en una de las mejores zonas de santiago. Contacte para más detalles y agendar una visita.",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Galeria de Imágenes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(modifier = Modifier.weight(1f).height(100.dp).background(Color.LightGray))
                    Box(modifier = Modifier.weight(1f).height(100.dp).background(Color.LightGray))
                    Box(modifier = Modifier.weight(1f).height(100.dp).background(Color.LightGray))
                }
            }
        }
    }
}
