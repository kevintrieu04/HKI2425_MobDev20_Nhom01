package com.example.cinemaapp.data

data class Ticket (
    var movieId: String = "",
    val movieName: String = "",
    val imageUrl: String = "",
    val seat: String = "",
    val time: String = "",
    val date: String = "",
    val count: Int = 0,
)