package com.example.cinemaapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.cinemaapp.data.AdModel
import com.example.cinemaapp.data.Film
import com.example.cinemaapp.models.AdRepository
import com.example.cinemaapp.network.DatabaseManager
import kotlinx.coroutines.launch

sealed interface HomePageUiState {
    data class Success(val movies: List<Film>, val ads: List<AdModel>) : HomePageUiState
    data object Error : HomePageUiState
    data object Loading : HomePageUiState
}

class HomePageViewModel(private val ad_reo: AdRepository) : ViewModel() {
    var uiState : HomePageUiState by mutableStateOf(HomePageUiState.Loading)
    private set



    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            uiState = HomePageUiState.Loading
            uiState = try {
                val db = DatabaseManager()
                val movies = db.readFilm()
                HomePageUiState.Success(movies, ad_reo.fetchAd())
            } catch (e: Exception) {
                HomePageUiState.Error
            }
        }
    }
    fun updateRating(filmName: String, newRating: Double) {
        val currentState = uiState
        if (currentState is HomePageUiState.Success) {
            // Cập nhật rating trong danh sách phim
            val updatedMovies = currentState.movies.map { film ->
                if (film.name == filmName) {
                    film.copy(rating = newRating)
                } else {
                    film
                }
            }
            // Cập nhật trạng thái
            uiState = currentState.copy(movies = updatedMovies)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as HomePageApplication)
                val adRepo = application.ad_container.ad_repo
                HomePageViewModel(adRepo)
            }
        }
    }
}