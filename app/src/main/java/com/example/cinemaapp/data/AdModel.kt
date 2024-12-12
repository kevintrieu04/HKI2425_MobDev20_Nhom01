package com.example.cinemaapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdModel(
    val id:String,
    @SerialName(value = "img_src")val imgSrc: String,
)