package com.example.cinemaapp.models

import com.example.cinemaapp.R
import com.example.cinemaapp.network.NetworkAPI
import kotlinx.coroutines.delay

interface Repository {
    /** Fetches list of MarsPhoto from marsApi */
    suspend fun fetchData(): List<MovieModel>
}

class MovieRepository (private val api: NetworkAPI): Repository {
    override suspend fun fetchData() = api.getMovies()
}