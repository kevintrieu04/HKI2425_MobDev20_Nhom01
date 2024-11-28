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
import com.example.cinemaapp.models.Repository
import com.example.cinemaapp.network.DbConnect
import kotlinx.coroutines.launch

sealed interface HomePageUiState {
    data class Success(val movies: List<Film>, val ads: List<AdModel>) : HomePageUiState
    data object Error : HomePageUiState
    data object Loading : HomePageUiState
}

class HomePageViewModel() : ViewModel() {
    var uiState : HomePageUiState by mutableStateOf(HomePageUiState.Loading)
    private set

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            uiState = HomePageUiState.Loading
            uiState = try {
                val db = DbConnect()
                val movies = db.readFilm()
                HomePageUiState.Success(movies, emptyList())
            } catch (e: Exception) {
                HomePageUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as HomePageApplication)
                val repo = application.container.repo
                val adRepo = application.ad_container.ad_repo
                HomePageViewModel()
            }
        }
    }
}