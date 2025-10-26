package com.example.rentfage.data.local

// 1. Importamos la clase R, que es el 'mapa' de todos los recursos de la app (imágenes, textos, etc.).
// Es necesaria para poder referirnos a las imágenes que están en la carpeta 'drawable'.
import com.example.rentfage.R

// 2. Modificamos la "ficha" o "carnet" de cada casa.
data class Casa(
    val id: Int,
    val price: String,
    val address: String,
    val details: String,
    // NUEVA PROPIEDAD: Cada casa ahora debe tener un 'imageResId'.
    // Esto guardará el "DNI" o "ID" que Android le da a cada imagen que pones en la carpeta 'drawable'.
    // Es un número entero, por eso el tipo es 'Int'.
    val imageResId: Int,
    var isFavorite: Boolean = false
)

// lista de los detalle de las casas
val casasDeEjemplo = listOf(
    Casa(1, "UF 28.500", "Av. Vitacura, Vitacura, Santiago", "4 hab | 1 baño | 450 m²", R.drawable.casa1),
    Casa(2, "UF 35.000", "Camino La Dehesa, Lo Barnechea, Santiago", " 4 hab | 1 baño | 600 m² | Piscina", R.drawable.casa2),
    Casa(3, "UF 26.500", "San Damián, Las Condes, Santiago", "4 hab | 1 baño | 500 m² | Jardín amplio", R.drawable.casa3),
    Casa(4, "UF 18.000", "Isidora Goyenechea, Las Condes, Santiago", "3 hab | 1 baño | 220 m² | Penthouse", R.drawable.casa4)
)
