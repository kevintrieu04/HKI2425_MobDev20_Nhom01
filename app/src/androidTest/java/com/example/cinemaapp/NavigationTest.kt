package com.example.cinemaapp

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.example.cinemaapp.data.AdModel
import com.example.cinemaapp.data.Film
import com.example.cinemaapp.ui.HomeScreen
import com.example.cinemaapp.ui.navigation.AppRoute
import com.example.cinemaapp.ui.navigation.AppRouteName
import com.example.cinemaapp.viewmodels.HomePageUiState
import com.example.cinemaapp.viewmodels.HomePageViewModel
import org.junit.Rule
import org.junit.Test

class NavigationTest {

    var movies:List<Film> = emptyList()
    var ads:List<AdModel> = emptyList()


    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testStartDestinationIsHome() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            AppRoute.GenerateRoute(
                navController,
                uiState = HomePageUiState.Success(movies,ads),
                viewModel = HomePageViewModel() )
        }

        // Kiểm tra màn hình Home hiển thị
        composeTestRule.onNodeWithText("Trang chủ").assertExists()
    }

    @Test
    fun testNavigateToLogin() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            AppRoute.GenerateRoute(
                navController,
                uiState = HomePageUiState.Success(emptyList(), emptyList()),
                viewModel = HomePageViewModel()
            )
        }

        // Nhấn vào nút chuyển sang Login (giả định nút có text "Go to Login")
        composeTestRule.onNodeWithText("Bạn chưa đăng nhập").performClick()

        // Kiểm tra LoginPage hiển thị
        composeTestRule.onNodeWithText("Login").assertExists()
    }


    @Test
    fun testNavigateToSearchWithCategory() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            AppRoute.GenerateRoute(
                navController,
                uiState = HomePageUiState.Success(movies,ads),
                viewModel = HomePageViewModel())
        }
        composeTestRule.onNodeWithText("Xem thêm").performClick()
        composeTestRule.onNodeWithText("Tìm kiếm").assertExists()
    }
    @Test
    fun homeScreen_displaysTitle() {
        composeTestRule.setContent {
            HomeScreen(
                uiState = HomePageUiState.Success(
                    ads = listOf(),
                    movies = listOf(),
                ),
                navController = rememberNavController(),
                viewModel = HomePageViewModel()
            )
        }

        composeTestRule.onNodeWithText("Trang chủ").assertExists()
    }
    @Test
    fun homeScreen_showsLoadingState() {
        composeTestRule.setContent {
            HomeScreen(
                uiState = HomePageUiState.Loading,
                navController = rememberNavController(),
                viewModel = HomePageViewModel()
            )
        }

        composeTestRule.onNodeWithContentDescription("Loading").assertExists()
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
                viewModel = HomePageViewModel()
            )
        }

        // Bấm vào nút mở drawer
        composeTestRule.onNodeWithContentDescription("Localized description").performClick()

        // Kiểm tra drawer menu được mở
        composeTestRule.onNodeWithText("Bạn chưa đăng nhập").assertExists()
    }


}