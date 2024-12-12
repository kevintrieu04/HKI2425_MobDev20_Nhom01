package com.example.cinemaapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cinemaapp.R
import com.example.cinemaapp.data.Film
import com.example.cinemaapp.network.DbConnect
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SearchUiState(
    val film_name: String = "",
    val film_type: String = "- Tất cả -",
    val film_category: String = "- Tất cả -",
    val film_country: String = "- Tất cả -",
    val film_year: String = "- Tất cả -",
    val film_duration: String = "- Tất cả -",
    val sort_by: String = "- Tất cả -",
)

/**
class SearchScreenViewModel (): ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _searchList = MutableStateFlow(FilmRepo.filmList)
    val searchList: StateFlow<List<Film>> = _searchList.asStateFlow()



    private fun updateName(name: String) {
        _uiState.update {
            it.copy(film_name = name)
        }
        _searchList.update {
            FilmRepo.filmList.filter { it.name.contains(name) }
        }
    }

    private fun updateType(type: String) {
        _uiState.update {
            it.copy(film_type = type)
        }
        _searchList.update {
            FilmRepo.filmList.filter { it.type == type }.toMutableStateList()
        }
    }


    private fun updateCategory(category: String) {
        _uiState.update {
            it.copy(film_category = category)
        }
        _searchList.update {
            FilmRepo.filmList.filter { it.genre == category }.toMutableStateList()
        }
    }

    private fun updateCountry(country: String) {
        _uiState.update {
            it.copy(film_country = country)
        }
        _searchList.update {
            FilmRepo.filmList.filter { it.country == country }.toMutableStateList()
        }
    }

    private fun updateYear(year: String) {
        _uiState.update {
            it.copy(film_year = year)
        }
        _searchList.update {
            FilmRepo.filmList.filter { it.year == year }.toMutableStateList()
        }
    }

    private fun updateDuration(duration: String) {
        _uiState.update {
            it.copy(film_duration = duration)
        }
        _searchList.update {
            FilmRepo.filmList.filter {
                if (duration == "Dưới 1 giờ") {
                    return@filter (it.duration < 60)
                } else if (duration == "1-2 giờ") {
                    return@filter (it.duration in 60..120)
                } else {
                    return@filter (it.duration > 120)
                }
            }.toMutableStateList()
        }

    }

    private fun updateSort(sort: String) {
        _uiState.update {
            it.copy(sort_by = sort)
        }
        _searchList.update {
           if (sort == "Ngày cập nhật") {
                FilmRepo.filmList.sortedBy { it.key }.toMutableStateList()
            } else if (sort == "Tên A-Z") {
                FilmRepo.filmList.sortedBy { it.name }.toMutableStateList()
            } else if (sort == "Tên Z-A") {
                FilmRepo.filmList.sortedByDescending { it.name }.toMutableStateList()
            } else {
                FilmRepo.filmList.sortedByDescending { it.rating }.toMutableStateList()
            }
        }

    }

    fun updateQuery(type: String = _uiState.value.film_type,
                    category: String = _uiState.value.film_category,
                    country: String = _uiState.value.film_country,
                    year: String = _uiState.value.film_year,
                    duration: String = _uiState.value.film_duration,
                    sort: String = _uiState.value.sort_by) {
        viewModelScope.launch {
            resetQuery()
            updateType(type)
            updateCategory(category)
            updateCountry(country)
            updateYear(year)
            updateDuration(duration)
            updateSort(sort)
        }

    }

    private fun resetQuery() {
        _uiState.update {
            SearchUiState()
        }
        _searchList.update {
            FilmRepo.filmList.toMutableStateList()
        }
    }
}
*/

class SearchScreenViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()
    private val _searchList = MutableStateFlow(FilmRepo.filmList)
    val searchList: StateFlow<List<Film>> = _searchList.asStateFlow()

    fun updateQuery(
        name: String = _uiState.value.film_name,
        type: String = _uiState.value.film_type,
        category: String = _uiState.value.film_category,
        country: String = _uiState.value.film_country,
        year: String = _uiState.value.film_year,
        duration: String = _uiState.value.film_duration,
        sort: String = _uiState.value.sort_by
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    film_name = name,
                    film_type = type,
                    film_category = category,
                    film_country = country,
                    film_year = year,
                    film_duration = duration,
                    sort_by = sort
                )
            }

            _searchList.update {
                FilmRepo.filmList
                    .filter { it.name.contains(name, ignoreCase = true) }
                    .filter { type == "- Tất cả -" || it.type == type }
                    .filter { category == "- Tất cả -" || it.genre == category }
                    .filter { country == "- Tất cả -" || it.country == country }
                    //.filter { year == "- Tất cả -" || it.year == year.toIntOrNull() }
                    .filter { duration == "- Tất cả -" || matchesDuration(it.duration, duration) }
                    .let { it ->
                        when (sort) {
                            "Ngày cập nhật" -> it.sortedBy { it.id }
                            "Tên A-Z" -> it.sortedBy { it.name }
                            "Tên Z-A" -> it.sortedByDescending { it.name }
                            else -> it.sortedByDescending { it.rating }
                        }
                    }
            }
        }
    }

    fun resetSearchState() {
        _uiState.update { SearchUiState() }
        _searchList.update { FilmRepo.filmList }
    }
    init {
        fetchData()
    }
    private fun fetchData() {
        viewModelScope.launch {
            val db = DbConnect()
            FilmRepo.filmList = db.readFilm()
        }
    }

}

private fun matchesDuration(duration: Int, filter: String): Boolean {
    return when (filter) {
        "Dưới 1 giờ" -> duration < 60
        "1-2 giờ" -> duration in 60..120
        "Trên 2 giờ" -> duration > 120
        else -> true
    }
}

object FilmRepo {
    var filmList = listOf<Film>()
}