package com.example.cinemaapp.network

import com.example.cinemaapp.models.AdModel
import com.example.cinemaapp.models.MovieModel
import com.example.cinemaapp.models.SearchModel
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * A public interface that exposes the [getMovies] method
 */
interface NetworkAPI {
    @GET("movie")
    suspend fun getMovies(): List<MovieModel>

    @GET("ads")
    suspend fun getAds(): List<AdModel>

//    search call API
    @GET("search")
    suspend fun getSearchMovies(): List<SearchModel>

}