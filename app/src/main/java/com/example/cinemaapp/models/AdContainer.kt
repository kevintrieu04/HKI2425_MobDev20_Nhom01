package com.example.cinemaapp.models

import com.example.cinemaapp.network.NetworkAPI
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AdContainer {
    val ad_repo : AdvertisementRepository
}

class DefaultAdContainer : AdContainer {

    private val baseUrl = "http://10.0.2.2:5000/"

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
    override val ad_repo: AdvertisementRepository by lazy {
        AdvertisementRepository(retrofitService)
    }
}