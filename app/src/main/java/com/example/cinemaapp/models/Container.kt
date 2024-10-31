package com.example.cinemaapp.models

import com.example.cinemaapp.network.NetworkAPI
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface Container {
    val repo : MovieRepository
}

class DefaultAppContainer : Container {

    private val baseUrl = "https://drive.google.com/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    /**
     * Retrofit service object for creating api calls
     */
    private val retrofitService: NetworkAPI by lazy {
        retrofit.create(NetworkAPI::class.java)
    }
    override val repo: MovieRepository by lazy {
        MovieRepository(retrofitService)
    }
}