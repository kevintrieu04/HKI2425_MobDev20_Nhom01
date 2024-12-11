package com.example.cinemaapp.models

import com.example.cinemaapp.network.NetworkAPI

interface SearchRepository {
    suspend fun fetchSearch(): List<SearchModel>
}

class SearchRepositoryImpl(private val api: NetworkAPI): SearchRepository {
    override suspend fun fetchSearch() = api.getSearchMovies()
}