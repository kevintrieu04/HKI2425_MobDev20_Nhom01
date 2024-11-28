package com.example.cinemaapp.data

data class Film(
    var id: String = "",
    val name: String = "",
    val year: Int = 0,
    val genre: Any = "",
    val ageRating: String = "",
    val rating: Double = 0.0,
    val country: String = "",
    val views: Int = 0,
    val description: String = "",
    val imgSrc: String = ""
){
    fun getGenreAsList(): List<String> {
        return when (genre) {
            is String -> listOf(genre)
            is List<*> -> genre.filterIsInstance<String>() // Nếu là danh sách, lọc các chuỗi
            else -> emptyList()
        }
    }
}
