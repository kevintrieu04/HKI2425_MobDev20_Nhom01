package com.example.cinemaapp.models

import com.example.cinemaapp.data.AdModel
import com.example.cinemaapp.network.NetworkAPI

interface AdRepository {
    suspend fun fetchAd(): List<AdModel>
}

class AdvertisementRepository (private val api: NetworkAPI): AdRepository {
    override suspend fun fetchAd() = api.getAds()
}