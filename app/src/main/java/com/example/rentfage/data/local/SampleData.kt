package com.example.rentfage.data.local

// "Molde" para una casa, ahora con nombre en español.
data class Casa(
    val id: Int,
    val price: String,
    val address: String,
    val details: String
)

// Lista de casas de ejemplo, ahora con nombre en español y accesible desde toda la app.
val casasDeEjemplo = listOf(
    Casa(1, "UF 28.500", "Av. Vitacura, Vitacura, Santiago", "4 hab | 5 baños | 450 m²"),
    Casa(2, "UF 35.000", "Camino La Dehesa, Lo Barnechea, Santiago", "5 hab | 6 baños | 600 m² | Piscina"),
    Casa(3, "UF 26.500", "San Damián, Las Condes, Santiago", "4 hab | 4 baños | 500 m² | Jardín amplio"),
    Casa(4, "UF 18.000", "Isidora Goyenechea, Las Condes, Santiago", "3 hab | 3 baños | 220 m² | Penthouse")
)
