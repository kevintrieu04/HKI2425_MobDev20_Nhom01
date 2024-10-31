package com.example.cinemaapp.models

import com.example.cinemaapp.network.NetworkAPI

interface Repository {

    suspend fun fetchData(): List<MovieModel>
}

class MovieRepository (private val api: NetworkAPI): Repository {
    override suspend fun fetchData() = api.getMovies()
}