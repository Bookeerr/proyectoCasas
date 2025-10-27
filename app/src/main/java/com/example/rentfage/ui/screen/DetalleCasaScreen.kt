package com.example.rentfage.ui.screen

import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.rentfage.R
import com.example.rentfage.data.local.casasDeEjemplo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DetalleCasaScreen(
    casaId: Int,
    onGoHome: () -> Unit // accion para poder volver al Home.
) {
    var showPurchaseSummary by rememberSaveable { mutableStateOf(false) }

    // Creamos la parte de confirmacion, por si paso a apretar el boton de comprar
    var showConfirmationDialog by rememberSaveable { mutableStateOf(false) }
    // CoroutineScope para poder lanzar tareas con retraso.
    val scope = rememberCoroutineScope()

    // Usamos Crossfade para animar el cambio entre las dos vistas.
    Crossfade(targetState = showPurchaseSummary, label = "PurchaseScreenAnimation") { isSummaryVisible ->

        if (isSummaryVisible) {
            // resumen de paso
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Proceso de pago", // titulo
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
                            Text("1. Un asesor te contactara para más detalles.", style = MaterialTheme.typography.bodyLarge)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("2. Recibiras contrato preliminar para firma.", style = MaterialTheme.typography.bodyLarge)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("3. Coordinaremos visita o inspeccion.", style = MaterialTheme.typography.bodyLarge)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("4. Te ayudamos con tramites y financiamiento.", style = MaterialTheme.typography.bodyLarge)
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                        Button(onClick = onGoHome) {
                            Text("Volver a propiedades")
                        }
                    }
                }
            }
        } else {
            // detalles de casas
            val casa = casasDeEjemplo.find { it.id == casaId }

            if (casa == null) {
                Text("Casa no encontrada.", modifier = Modifier.padding(16.dp))
                return@Crossfade
            }

            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())
            ) {
                Image(painter = painterResource(id = casa.imageResId), contentDescription = "Imagen de la casa", modifier = Modifier.fillMaxWidth().height(250.dp), contentScale = ContentScale.Crop)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = casa.price, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = casa.address, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = casa.details, style = MaterialTheme.typography.bodyLarge)

                val galeriaDeImagenes = when (casa.id) {
                    1 -> listOf(R.drawable.dormitorio1, R.drawable.comedor1, R.drawable.cocina1, R.drawable.bano1)
                    2 -> listOf(R.drawable.dormitorio2, R.drawable.comedor2, R.drawable.cocina2, R.drawable.bano2)
                    3 -> listOf(R.drawable.dormitorio3, R.drawable.comedor3, R.drawable.cocina3, R.drawable.bano3)
                    4 -> listOf(R.drawable.dormitorio4, R.drawable.comedor4, R.drawable.cocina4, R.drawable.bano4)
                    else -> emptyList()
                }

                if (galeriaDeImagenes.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(text = "Galería", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(galeriaDeImagenes) { imageId ->
                            Image(painter = painterResource(id = imageId), contentDescription = "Foto de la galería", modifier = Modifier.height(120.dp).width(160.dp).clip(MaterialTheme.shapes.medium), contentScale = ContentScale.Crop)
                        }
                    }
                }

                // formas de pago
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "Formas de Pago", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                // Simplemente listamos los textos uno debajo del otro.
                Text("Tarjeta de Credito")
                Spacer(modifier = Modifier.height(4.dp))
                Text("Transferencia Bancaria")
                Spacer(modifier = Modifier.height(4.dp))
                Text("Credito Hipotecario")

                Spacer(modifier = Modifier.height(32.dp))
                val context = LocalContext.current
                Button(
                    onClick = {
                        //mostramos la ventana de confirmacion.
                        showConfirmationDialog = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Comprar esta propiedad")
                }
            }
        }
    }

    // Este bloque de código se encarga de mostrar la ventana de dialogo.
    if (showConfirmationDialog) {
        AlertDialog(
            // Acción que se ejecuta si el usuario pulsa fuera de la ventana.
            onDismissRequest = { showConfirmationDialog = false },
            // El titulo de la ventana.
            title = { Text("Confirmación de Compra") },
            // El mensaje principal de la pregunta.
            text = { Text("¿Estás seguro de comprar esta propiedad?") },
            // El boton de "Aceptar" o confirmacion.
            confirmButton = {
                TextButton(
                    onClick = {
                        // Novedad: Usamos el 'scope' para lanzar una tarea con retraso.
                        scope.launch {
                            showConfirmationDialog = false // Cerramos la ventana de inmediato.
                            delay(300) // Esperamos un instante (300 milisegundos).
                            showPurchaseSummary = true    //Activamos la animación de Crossfade.
                        }
                    }
                ) {
                    Text("Aceptar")
                }
            },
            // El boton de "Cancelar" o rechazo.
            dismissButton = {
                //boton para cancelar la acción.
                TextButton(
                    // Al hacer clic, simplemente cierra la ventana y no hace nada más.
                    onClick = { showConfirmationDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
