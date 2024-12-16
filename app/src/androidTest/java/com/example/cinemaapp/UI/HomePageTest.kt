package com.example.cinemaapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.example.cinemaapp.data.AdModel
import com.example.cinemaapp.data.Film
import com.example.cinemaapp.viewmodels.HomePageUiState
import com.example.cinemaapp.viewmodels.HomePageViewModel
import com.example.cinemaapp.viewmodels.SearchScreenViewModel
import org.junit.Rule
import org.junit.Test

class HomePageTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testHomePageDisplaysContentCorrectly() {
        val fakeAds = listOf(
            AdModel(id = "1", imgSrc = "https://example.com/ad1.jpg"),
            AdModel(id = "2", imgSrc = "https://example.com/ad2.jpg")
        )

        val fakeMovies = listOf(
            Film(
                id = "1",
                name = "Inception",
                genre = "Sci-Fi",
                imgSrc = "https://example.com/movie1.jpg",
                rating = 8.8,
                duration = 148
            ),
            Film(
                id = "2",
                name = "Interstellar",
                genre = "Sci-Fi",
                imgSrc = "https://example.com/movie2.jpg",
                rating = 8.6,
                duration = 169
            )
        )

        composeTestRule.setContent {
            TestableHomeScreen(
                ads = fakeAds,
                movies = fakeMovies
            )
        }

        composeTestRule.onNodeWithText("Chào mừng bạn trở lại!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Inception").assertIsDisplayed()
        composeTestRule.onNodeWithText("Interstellar").assertIsDisplayed()
        composeTestRule.onNodeWithText("Thể loại").assertIsDisplayed()

        composeTestRule.onNodeWithText("Xem thêm").performClick()
    }

    @Composable
    private fun TestableHomeScreen(ads: List<AdModel>, movies: List<Film>) {
        val navController = rememberNavController()
        val dummyViewModel = HomePageViewModel(
            ad_reo = TODO()
        )
        val dummySearchViewModel = SearchScreenViewModel()

        HomeScreen(
            uiState = HomePageUiState.Success(ads = ads, movies = movies),
            navController = navController,
            viewModel = dummyViewModel,
            searchViewModel = dummySearchViewModel
        )
    }
}
