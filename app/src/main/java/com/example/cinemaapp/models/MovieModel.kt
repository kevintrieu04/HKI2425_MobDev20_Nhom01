package com.example.cinemaapp.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieModel(
    val id:String,
    @SerialName(value = "title")val title: String,
    @SerialName(value = "img_src")val imgSrc: String,
    @SerialName(value = "type")val type: String,
    @SerialName(value = "duration")val duration: String,
    @SerialName(value = "rating")val rating: String,
    @SerialName(value = "synopsis")val synopsis: String,
    @SerialName(value = "isPlaying")val isPlaying: Boolean
)

