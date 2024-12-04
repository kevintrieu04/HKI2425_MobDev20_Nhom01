package com.example.cinemaapp.network

import com.example.cinemaapp.models.AdModel
import com.example.cinemaapp.models.MovieModel
import retrofit2.http.GET

/**
 * A public interface that exposes the [getMovies] method
 */
interface NetworkAPI {
    @GET("movie")
    suspend fun getMovies(): List<MovieModel>

    @GET("ads")
    suspend fun getAds(): List<AdModel>
}