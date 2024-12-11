package com.example.cinemaapp.models

import com.example.cinemaapp.network.NetworkAPI
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit


interface SearchContainer {
    val searchRepository: SearchRepositoryImpl
}
class DeafultSearchContainer: SearchContainer {

    private val baseUrl = "http://10.0.2.2:5000/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: NetworkAPI by lazy {
        retrofit.create(NetworkAPI::class.java)
    }
    override val searchRepository: SearchRepositoryImpl by lazy {
        SearchRepositoryImpl(retrofitService)
    }
}