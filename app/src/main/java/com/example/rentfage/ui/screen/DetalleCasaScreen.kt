package com.example.rentfage.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.rentfage.R // Se importa la clase R
import com.example.rentfage.data.local.casasDeEjemplo

@Composable
fun DetalleCasaScreen(casaId: Int) {
    // Buscamos la casa correcta usando el ID que nos llega.
    val casa = casasDeEjemplo.find { it.id == casaId }

    //Si la casa no se encuentra, mostramos un mensaje de error.
    if (casa == null) {
        Text("Casa no encontrada.", modifier = Modifier.padding(16.dp))
        return // Detenemos la ejecucion
    }

    // Ahora usamos una Columna que permite el scroll vertical general.
    Column(
        modifier = Modifier
            .fillMaxSize() // La columna ocupa toda la pantalla.
            .padding(16.dp) // Añadimos un margen general.
            .verticalScroll(rememberScrollState()) // Hacemos que toda la columna sea deslizable verticalmente.
    ) {
        // Mostramos la imagen real de la casa en la parte superior.
        Image(
            painter = painterResource(id = casa.imageResId),
            contentDescription = "Imagen de la casa",
            modifier = Modifier
                .fillMaxWidth() // La imagen ocupa todo el ancho.
                .height(250.dp), // Le damos una altura fija.
            contentScale = ContentScale.Crop // La imagen se recorta para llenar el espacio sin deformarse.
        )

        // Añadimos un espacio para separar la imagen del texto.
        Spacer(modifier = Modifier.height(16.dp))

        // Mostramos el precio con un estilo grande y en negrita.
        Text(
            text = casa.price,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        // Espacio.
        Spacer(modifier = Modifier.height(8.dp))

        // Mostramos la direccion con un estilo un poco más pequeño.
        Text(
            text = casa.address,
            style = MaterialTheme.typography.titleLarge
        )

        // Espacio.
        Spacer(modifier = Modifier.height(16.dp))

        // Mostramos los detalles (habitaciones, baños, etc.) con el estilo de letra normal.
        Text(
            text = casa.details,
            style = MaterialTheme.typography.bodyLarge
        )

        // --- Novedad: Lógica para seleccionar una galería para cada casa ---

        // Usamos 'when' para decidir qué lista de imágenes usar según el ID de la casa.
        val galeriaDeImagenes = when (casa.id) {
            // la casa 1, usamos las imágenes de la casa 1.

            1 -> listOf(
                R.drawable.dormitorio1,
                R.drawable.comedor1,
                R.drawable.cocina1,
                R.drawable.bano1
            )
            // la casa 2, usamos las imágenes de la casa 2.
            2 -> listOf(
                R.drawable.dormitorio2,
                R.drawable.comedor2,
                R.drawable.cocina2,
                R.drawable.bano2
            )
            // la casa 3, usamos las imágenes de la casa 3.
            3 -> listOf(
                R.drawable.dormitorio3,
                R.drawable.comedor3,
                R.drawable.cocina3,
                R.drawable.bano3

            )

            4 -> listOf(
                R.drawable.dormitorio4,
                R.drawable.comedor4,
                R.drawable.cocina4,
                R.drawable.bano4
            )

        }

        // 2. Si la lista de imágenes para la galería no está vacía, la mostramos.
        if (galeriaDeImagenes.isNotEmpty()) {
            // Espacio grande antes de la galería.
            Spacer(modifier = Modifier.height(24.dp))

            // Título para la sección de la galería.
            Text(
                text = "Galeria",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // Espacio antes de las fotos.
            Spacer(modifier = Modifier.height(8.dp))

            // 3. La LazyRow ahora usa la lista que hemos seleccionado en el 'when'.
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(galeriaDeImagenes) { imageId ->
                    Image(
                        painter = painterResource(id = imageId),
                        contentDescription = "Foto de la galeria",
                        modifier = Modifier
                            .height(120.dp)
                            .width(160.dp)
                            .clip(MaterialTheme.shapes.medium),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}
