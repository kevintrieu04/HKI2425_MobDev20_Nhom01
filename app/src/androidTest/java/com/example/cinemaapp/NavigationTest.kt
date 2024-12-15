package com.example.cinemaapp

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.cinemaapp.data.AdModel
import com.example.cinemaapp.data.Film
import com.example.cinemaapp.ui.HomeScreen
import com.example.cinemaapp.ui.navigation.AppRoute
import com.example.cinemaapp.ui.navigation.AppRouteName
import com.example.cinemaapp.viewmodels.HomePageUiState
import com.example.cinemaapp.viewmodels.HomePageViewModel
import com.example.cinemaapp.viewmodels.SearchScreenViewModel
import okhttp3.internal.wait
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
                homePageViewModel = viewModel(factory = HomePageViewModel.Factory),
                searchViewModel = SearchScreenViewModel())
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
                uiState = HomePageUiState.Success(movies,ads),
                homePageViewModel = viewModel(factory = HomePageViewModel.Factory),
                searchViewModel = SearchScreenViewModel())
        }
        composeTestRule.onNodeWithContentDescription("Localized description").performClick()
        // Nhấn vào nút chuyển sang Login (giả định nút có text "Go to Login")
        composeTestRule.onNodeWithText("Đăng xuất").performClick()
        composeTestRule.onNodeWithText("Bạn chưa đăng nhập").performClick()

        // Kiểm tra LoginPage hiển thị
        composeTestRule.onNodeWithText("Login").assertExists()
    }


    @Test
    fun testNavigateToSearch() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            AppRoute.GenerateRoute(
                navController,
                uiState = HomePageUiState.Success(movies,ads),
                homePageViewModel = viewModel(factory = HomePageViewModel.Factory),
                searchViewModel = SearchScreenViewModel())
        }
        composeTestRule.onNodeWithContentDescription("Button 1").performClick()
        composeTestRule.onNodeWithText("Tìm kiếm").assertExists()
    }

    @Test
    fun testNavigateToSearchWithCategory() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            AppRoute.GenerateRoute(
                navController,
                uiState = HomePageUiState.Success(movies,ads),
                homePageViewModel = viewModel(factory = HomePageViewModel.Factory),
                searchViewModel = SearchScreenViewModel())
        }
        composeTestRule.onNodeWithContentDescription("Hoạt hình").performClick()
        composeTestRule.onNodeWithText("Hoạt hình").assertExists()
    }

    @Test
    fun testNavigateToSearch2() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            AppRoute.GenerateRoute(
                navController,
                uiState = HomePageUiState.Success(movies,ads),
                homePageViewModel = viewModel(factory = HomePageViewModel.Factory),
                searchViewModel = SearchScreenViewModel())
        }
        composeTestRule.onNodeWithContentDescription("Button 2").performClick()
        composeTestRule.onNodeWithText("Tìm kiếm").assertExists()

    }

    @Test
    fun testNavigateToSearch3() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            AppRoute.GenerateRoute(
                navController,
                uiState = HomePageUiState.Success(movies,ads),
                homePageViewModel = viewModel(factory = HomePageViewModel.Factory),
                searchViewModel = SearchScreenViewModel())
        }
        composeTestRule.onNodeWithContentDescription("Button 3").performClick()
        composeTestRule.onNodeWithText("Tìm kiếm").assertExists()

    }

    @Test
    fun testNavigateToSearch4() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            AppRoute.GenerateRoute(
                navController,
                uiState = HomePageUiState.Success(movies,ads),
                homePageViewModel = viewModel(factory = HomePageViewModel.Factory),
                searchViewModel = SearchScreenViewModel())
        }
        composeTestRule.onNodeWithContentDescription("Localized description").performClick()
        composeTestRule.onNodeWithText("Tìm kiếm").assertExists()
    }

    @Test
    fun testDetailScreen() {

        val uiStateHolder = mutableStateOf<HomePageUiState>(HomePageUiState.Loading)

        composeTestRule.setContent {
            val navController = rememberNavController()
            val viewModel: HomePageViewModel = viewModel(factory = HomePageViewModel.Factory)
            uiStateHolder.value = viewModel.uiState

            AppRoute.GenerateRoute(
                navController,
                uiState = viewModel.uiState,
                homePageViewModel = viewModel,
                searchViewModel = SearchScreenViewModel())
        }
        composeTestRule.waitUntil(timeoutMillis = 30000) {
            uiStateHolder.value is HomePageUiState.Success
        }
        composeTestRule.runOnIdle {
            Thread.sleep(2000) // Waits for 2 seconds
        }
        composeTestRule.onAllNodesWithContentDescription("Movie Image")[0].performClick()
        composeTestRule.onNodeWithText("Thông tin phim").assertExists()
    }


}