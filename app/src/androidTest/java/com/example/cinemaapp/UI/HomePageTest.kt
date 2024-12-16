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
import org.mockito.Mockito
import org.mockito.kotlin.whenever

class HomePageTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testHomePageDisplaysContentCorrectly() {
        // Arrange: Mock data and dependencies
        val mockAds = listOf(
            AdModel(id = "1", imgSrc = "https://example.com/ad1.jpg"),
            AdModel(id = "2", imgSrc = "https://example.com/ad2.jpg")
        )

        val mockMovies = listOf(
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

        val mockViewModel = Mockito.mock(HomePageViewModel::class.java)
        val mockSearchViewModel = Mockito.mock(SearchScreenViewModel::class.java)
        whenever(mockViewModel.uiState).thenReturn(
            HomePageUiState.Success(ads = mockAds, movies = mockMovies)
        )

        // Act: Set the content for the test
        composeTestRule.setContent {
            TestableHomeScreen(
                viewModel = mockViewModel,
                searchViewModel = mockSearchViewModel
            )
        }

        // Assert: Check if UI elements are displayed
        composeTestRule.onNodeWithText("Chào mừng bạn trở lại!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Inception").assertIsDisplayed()
        composeTestRule.onNodeWithText("Interstellar").assertIsDisplayed()
        composeTestRule.onNodeWithText("Thể loại").assertIsDisplayed()
        composeTestRule.onNodeWithText("Đang chiếu tại rạp").assertIsDisplayed()

        // Act: Simulate navigation by clicking "Xem thêm"
        composeTestRule.onNodeWithText("Xem thêm").performClick()
        Mockito.verify(mockSearchViewModel).updateQuery("- Tất cả -")
    }

    @Composable
    private fun TestableHomeScreen(
        viewModel: HomePageViewModel,
        searchViewModel: SearchScreenViewModel
    ) {
        val navController = rememberNavController()
        HomeScreen(
            uiState = viewModel.uiState,
            navController = navController,
            viewModel = viewModel,
            searchViewModel = searchViewModel
        )
    }
}
