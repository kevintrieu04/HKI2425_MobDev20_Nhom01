package com.example.cinemaapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cinemaapp.R
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
                    .filter { year == "- Tất cả -" || it.year == year }
                    .filter { duration == "- Tất cả -" || matchesDuration(it.duration, duration) }
                    .let {
                        when (sort) {
                            "Ngày cập nhật" -> it.sortedBy { it.key }
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

}

private fun matchesDuration(duration: Int, filter: String): Boolean {
    return when (filter) {
        "Dưới 1 giờ" -> duration < 60
        "1-2 giờ" -> duration in 60..120
        "Trên 2 giờ" -> duration > 120
        else -> true
    }
}
data class Film(
    val key: String = "",
    val name: String = "",
    val year: String = "",
    val type: String = "",
    val genre: String = "",
    val ageRating: String = "",
    val duration: Int = 0,
    val rating: Double = 0.0,
    val country: String = "",
    val views: Int = 0,
    val description: String = "",
    val imgSrc: String = R.drawable.minion.toString()
)

object FilmRepo {
    val filmList = listOf(
        Film(
            key = "1",
            name = "Film 1",
            year = "2021",
            type = "Phim Lẻ",
            genre = "Hành Động",
            ageRating = "PG-13",
            duration = 110,
            rating = 8.5,
            country = "USA",
            views = 1000,
            description = "Description 1"
        ),
        Film(
            key = "2",
            name = "Film 2",
            year = "2020",
            type = "Phim Lẻ",
            genre = "Hài",
            ageRating = "PG",
            duration = 50,
            rating = 7.2,
            country = "UK",
            views = 1500,
            description = "Description 2"
        ),
        Film(
            key = "3",
            name = "Film 3",
            year = "2019",
            type = "Phim Lẻ",
            genre = "Drama",
            ageRating = "R",
            duration = 130,
            rating = 9.0,
            country = "France",
            views = 2000,
            description = "Description 3"
        ),
        Film(
            key = "4",
            name = "Film 4",
            year = "2018",
            type = "Phim Lẻ",
            genre = "Kinh dị",
            ageRating = "PG-13",
            duration = 130,
            rating = 6.8,
            country = "Japan",
            views = 2500,
            description = "Description 4"
        ),
        Film(
            key = "5",
            name = "Film 5",
            year = "2017",
            type = "Phim Lẻ",
            genre = "Khoa học viễn tưởng",
            duration = 50,
            ageRating = "PG",
            rating = 8.0,
            country = "Canada",
            views = 3000,
            description = "Description 5"
        ),
        Film(
            key = "6",
            name = "Film 6",
            year = "2016",
            type = "Phim Bộ",
            duration = 90,
            genre = "Lãng mạn",
            ageRating = "PG-13",
            rating = 7.5,
            country = "India",
            views = 3500,
            description = "Description 6"
        ),
        Film(
            key = "7",
            name = "Film 7",
            year = "2015",
            type = "Phim Bộ",
            duration = 130,
            genre = "Hành động",
            ageRating = "R",
            rating = 8.3,
            country = "Germany",
            views = 4000,
            description = "Description 7"
        ),
        Film(
            key = "8",
            name = "Film 8",
            year = "2014",
            type = "Phim Bộ",
            duration = 90,
            genre = "Hoạt hình",
            ageRating = "G",
            rating = 9.1,
            country = "South Korea",
            views = 4500,
            description = "Description 8"
        ),
        Film(
            key = "9",
            name = "Film 9",
            year = "2013",
            type = "Phim Bộ",
            duration = 50,
            genre = "Khoa học viễn tưởng",
            ageRating = "PG",
            rating = 7.9,
            country = "Australia",
            views = 5000,
            description = "Description 9"
        ),
        Film(
            key = "10",
            name = "Film 10",
            year = "2012",
            type = "Phim Bộ",
            duration = 50,
            genre = "Tài liệu",
            ageRating = "PG",
            rating = 8.7,
            country = "Brazil",
            views = 5500,
            description = "Description 10"
        )
    )
    //var searchList = filmList.toMutableStateList()
}