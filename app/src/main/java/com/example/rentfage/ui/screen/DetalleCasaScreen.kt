package com.example.rentfage.ui.screen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.rentfage.ui.viewmodel.CasasViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DetalleCasaScreen(
    casaId: Int,
    onGoHome: () -> Unit,
    casasViewModel: CasasViewModel
) {
    var showPurchaseSummary by rememberSaveable { mutableStateOf(false) }
    var showConfirmationDialog by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val casasState by casasViewModel.uiState.collectAsState()
    val casa = casasState.casas.find { it.id == casaId }

    Crossfade(targetState = showPurchaseSummary, label = "PurchaseScreenAnimation") { isSummaryVisible ->
        if (isSummaryVisible) {
            // Novedad: Pantalla de confirmación de éxito.
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Éxito",
                            tint = Color(0xFF4CAF50), // Un color verde éxito.
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "¡Solicitud Enviada!",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
                            Text("1. Un asesor te contactará para más detalles.", style = MaterialTheme.typography.bodyLarge)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("2. Recibirás contrato preliminar para firma.", style = MaterialTheme.typography.bodyLarge)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("3. Coordinaremos visita o inspección.", style = MaterialTheme.typography.bodyLarge)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("4. Te ayudamos con trámites y financiamiento.", style = MaterialTheme.typography.bodyLarge)
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                        Button(onClick = onGoHome) { Text("Volver a propiedades") }
                    }
                }
            }
        } else {
            if (casa == null) {
                Text("Casa no encontrada.", modifier = Modifier.padding(16.dp))
                return@Crossfade
            }

            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())
            ) {
                Image(
                    painter = painterResource(id = casa.imageResId),
                    contentDescription = "Imagen de la casa",
                    modifier = Modifier.fillMaxWidth().height(250.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = casa.price, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = casa.address, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = casa.details, style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "Formas de Pago", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Tarjeta de Credito")
                Spacer(modifier = Modifier.height(4.dp))
                Text("Transferencia Bancaria")
                Spacer(modifier = Modifier.height(4.dp))
                Text("Credito Hipotecario")

                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { showConfirmationDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Comprar esta propiedad")
                }
            }
        }
    }

    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = { Text("Confirmación de Compra") },
            text = { Text("¿Estás seguro de comprar esta propiedad?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            showConfirmationDialog = false
                            delay(300)
                            showPurchaseSummary = true
                        }
                    }
                ) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmationDialog = false }) { Text("Cancelar") }
            }
        )
    }
}
