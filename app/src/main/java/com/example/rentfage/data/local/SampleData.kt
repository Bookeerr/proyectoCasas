package com.example.rentfage.data.local


data class Casa(
    val id: Int,
    val price: String,
    val address: String,
    val details: String
)

// la lista de casas.
val casasDeEjemplo = listOf(
    Casa(1, "UF 28.500", "Vitacura, Santiago", "4 hab | 5 baños | 450 m²"),
    Casa(2, "UF 35.000", "Lo Barnechea, Santiago", "5 hab | 6 baños | 600 m² | Piscina"),
    Casa(3, "UF 26.500", "Las Condes, Santiago", "4 hab | 4 baños | 500 m² | Jardín amplio"),
    Casa(4, "UF 18.000", "Las Condes, Santiago", "3 hab | 3 baños | 220 m² | Penthouse")
)
