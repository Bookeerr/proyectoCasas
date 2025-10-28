package com.example.rentfage.data.local

import com.example.rentfage.R

// Novedad: Volvemos al sistema simple donde la imagen es un ID de recurso (Int).
// Añadimos de nuevo latitud y longitud para la funcionalidad de ubicación.
data class Casa(
    val id: Int,
    val price: String,
    val address: String,
    val details: String,
    val imageResId: Int, // Volvemos a usar un Int para la imagen.
    val latitude: Double,
    val longitude: Double,
    var isFavorite: Boolean = false
)

// Novedad: La lista de ejemplo vuelve a ser una variable simple.
val casasDeEjemplo: List<Casa> = listOf(
    Casa(1, "UF 28.500", "Av. Vitacura, Vitacura, Santiago", "4 hab | 1 baño | 450 m²", R.drawable.casa1, -33.4130, -70.5947),
    Casa(2, "UF 35.000", "Camino La Dehesa, Lo Barnechea, Santiago", " 4 hab | 1 baño | 600 m² | Piscina", R.drawable.casa2, -33.3592, -70.5150),
    Casa(3, "UF 26.500", "San Damián, Las Condes, Santiago", "4 hab | 1 baño | 500 m² | Jardín amplio", R.drawable.casa3, -33.3989, -70.5303),
    Casa(4, "UF 18.000", "Isidora Goyenechea, Las Condes, Santiago", "3 hab | 1 baño | 220 m² | Penthouse", R.drawable.casa4, -33.4100, -70.5986)
)
