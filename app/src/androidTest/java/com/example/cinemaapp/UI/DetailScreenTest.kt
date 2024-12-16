package com.example.cinemaapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.cinemaapp.data.Film
import com.example.cinemaapp.viewmodels.HomePageViewModel
import org.junit.Rule
import org.junit.Test


class DetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testDetailScreenDisplaysMovieDetails() {
        // Arrange: Mock data and dependencies
        val mockFilm = Film(
            id = "1",
            type = "Movie",
            name = "Inception",
            year = 2010,
            genre = "Sci-Fi",
            ageRating = "PG-13",
            rating = 8.8,
            country = "USA",
            views = 1000000,
            description = "A thief who steals corporate secrets through the use of dream-sharing technology...",
            imgSrc = "https://example.com/image.jpg",
            isPlaying = "Yes",
            duration = 148
        )



        // Act: Set the content and load the screen
        composeTestRule.setContent {

        }

        // Assert: Check if movie details are displayed
        composeTestRule.onNodeWithText("Inception").assertExists()
        composeTestRule.onNodeWithText("Sci-Fi").assertExists()
        composeTestRule.onNodeWithText("148 phút").assertExists()
        composeTestRule.onNodeWithText("A thief who steals corporate secrets through the use of dream-sharing technology...").assertExists()
    }

    @Composable
    private fun TestableDetailScreen(film: Film, homePageViewModel: HomePageViewModel) {
        val navController = rememberNavController()
        DetailScreen(
            navController = navController,
            movie = film,
            homePageViewModel = homePageViewModel
        )
    }
}
