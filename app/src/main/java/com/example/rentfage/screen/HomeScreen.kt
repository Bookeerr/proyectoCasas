package com.example.rentfage.screen

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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

// Ahora sí, una réplica exacta del código del profesor.
@Composable
fun HomeScreen(
    onGoLogin: () -> Unit,
    onGoRegister: () -> Unit
) {
    val bg = MaterialTheme.colorScheme.surfaceVariant // Fondo agradable para Home

    Box( // Contenedor a pantalla completa
        modifier = Modifier
            .fillMaxSize() // Ocupa todo
            .background(bg) // Aplica fondo
            .padding(16.dp), // Margen interior
        contentAlignment = Alignment.Center // Centra contenido
    ) {
        Column( // Estructura vertical
            horizontalAlignment = Alignment.CenterHorizontally // Centra hijos
        ) {
            // Cabecera como Row
            Row(
                verticalAlignment = Alignment.CenterVertically // Centra vertical
            ) {
                Text( // Título Home
                    text = "Home",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.width(8.dp)) // Separación horizontal
                AssistChip( // Chip decorativo
                    onClick = {},
                    label = { Text("Navega desde arriba o aquí") }
                )
            }

            Spacer(Modifier.height(20.dp)) // Separación

            // Tarjeta con un mini “hero”
            ElevatedCard(
                modifier = Modifier.fillMaxWidth() // Ancho completo
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Demostración de navegación con TopBar + Drawer + Botones",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(12.dp)) // Separación
                    Text(
                        "Usa la barra superior (íconos y menú), el menú lateral o estos botones.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(Modifier.height(24.dp)) // Separación

            // Botones de navegación
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp) // Espacio entre botones
            ) {
                Button(onClick = onGoLogin) { Text("Ir a Login") }
                OutlinedButton(onClick = onGoRegister) { Text("Ir a Registro") }
            }
        }
    }
}