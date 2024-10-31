package com.example.cinemaapp.viewmodels

import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.cinemaapp.models.AdModel
import com.example.cinemaapp.models.AdRepository
import com.example.cinemaapp.models.AdvertisementRepository
import com.example.cinemaapp.models.MovieModel
import com.example.cinemaapp.models.MovieRepository
import com.example.cinemaapp.models.Repository
import kotlinx.coroutines.launch

sealed interface HomePageUiState {
    data class Success(val movies: List<MovieModel>, val ads: List<AdModel>) : HomePageUiState
    data object Error : HomePageUiState
    data object Loading : HomePageUiState
}

class HomePageViewModel(private val repo: Repository,
                        private val ad_repo: AdRepository) : ViewModel() {
    var uiState : HomePageUiState by mutableStateOf(HomePageUiState.Loading)
    private set

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            uiState = HomePageUiState.Loading
            uiState = try {
                HomePageUiState.Success(repo.fetchData(), ad_repo.fetchAd())
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
                val ad_repo = application.ad_container.ad_repo
                HomePageViewModel(repo = repo, ad_repo = ad_repo)
            }
        }
    }
}