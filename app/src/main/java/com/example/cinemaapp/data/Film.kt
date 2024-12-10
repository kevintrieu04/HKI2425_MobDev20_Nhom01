package com.example.cinemaapp.data

import kotlin.time.Duration

data class Film(
    var id: String = "",
    val name: String = "",
    val year: Int = 0,
    val genre: String = "",
    val ageRating: String = "",
    val rating: Double = 0.0,
    val country: String = "",
    val views: Int = 0,
    val description: String = "",
    val imgSrc: String = "",
    val isPlaying: Boolean = false,
    val duration: Int = 0,
)

