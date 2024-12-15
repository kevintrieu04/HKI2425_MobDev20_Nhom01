package com.example.cinemaapp

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.cinemaapp.data.AdModel
import com.example.cinemaapp.data.Film
import com.example.cinemaapp.models.AdRepository
import com.example.cinemaapp.models.AdvertisementRepository
import com.example.cinemaapp.network.NetworkAPI
import com.example.cinemaapp.ui.Banners
import com.example.cinemaapp.ui.HomeScreen
import com.example.cinemaapp.viewmodels.HomePageUiState
import com.example.cinemaapp.viewmodels.HomePageViewModel
import com.example.cinemaapp.viewmodels.SearchScreenViewModel
import org.junit.Rule
import org.junit.Test
class HomePageUiTest {
    var movies:List<Film> = emptyList()
    var ads:List<AdModel> = emptyList()


    @get:Rule
    val composeTestRule = createComposeRule()
    @Test
    fun homeScreen_displaysTitle() {
        composeTestRule.setContent {
            HomeScreen(
                uiState = HomePageUiState.Success(
                    ads = listOf(),
                    movies = listOf(),
                ),
                navController = rememberNavController(),
                viewModel =  viewModel(factory = HomePageViewModel.Factory),
                searchViewModel = SearchScreenViewModel()
            )
        }

        composeTestRule.onNodeWithText("Trang chủ").assertExists()
    }

    @Test
    fun homeScreen_opensDrawerMenu() {
        composeTestRule.setContent {
            HomeScreen(
                uiState = HomePageUiState.Success(
                    ads = listOf(),
                    movies = listOf()
                ),
                navController = rememberNavController(),
                viewModel =  viewModel(factory = HomePageViewModel.Factory),
                searchViewModel = SearchScreenViewModel()
            )
        }

        // Bấm vào nút mở drawer
        composeTestRule.onNodeWithContentDescription("Localized description").performClick()

        // Kiểm tra drawer menu được mở
        composeTestRule.onNodeWithText("Bạn chưa đăng nhập").assertExists()
    }


}