package com.example.cinemaapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.cinemaapp.data.Film
import org.junit.Rule
import org.junit.Test

class DetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testDetailScreenAlwaysDisplaysCorrectly() {
        // Arrange: Dữ liệu giả (mock data)
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



        composeTestRule.onNodeWithText("Inception").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sci-Fi").assertIsDisplayed()
        composeTestRule.onNodeWithText("148 phút").assertIsDisplayed()
        composeTestRule.onNodeWithText("A thief who steals corporate secrets through the use of dream-sharing technology...").assertIsDisplayed()
    }


}
