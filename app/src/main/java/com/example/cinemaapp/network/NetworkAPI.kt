package com.example.cinemaapp.network

import com.example.cinemaapp.data.AdModel
import com.example.cinemaapp.data.MovieModel
import retrofit2.http.GET

/**
 * A public interface that exposes the [getMovies] method
 */
interface NetworkAPI {
    @GET("uc?export=download&id=135kmAWeGUhTEzvt4V8mdC0CE2sHoKkQU")
    suspend fun getMovies(): List<MovieModel>

    @GET("uc?export=download&id=1E7NXafW7JsjKSPRXGK5-8tEpZQOPrsdF")
    suspend fun getAds(): List<AdModel>


}