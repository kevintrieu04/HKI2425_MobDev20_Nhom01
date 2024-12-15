package com.example.cinemaapp

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
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
import com.example.compose.AppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.junit.Rule
import org.junit.Test

class NavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testStartDestinationIsHome() {
        composeTestRule.setContent {
            /// from Accompanist, setting a status bar
            val systemUiController = rememberSystemUiController()
            systemUiController.setStatusBarColor(
                color = Color.Transparent,
                darkIcons = true,
            )
            systemUiController.setNavigationBarColor(
                color = Color.Transparent,
                darkIcons = true,
            )


            AppTheme {
                /// main navigation
                val navController = rememberNavController()
                val homePageViewModel: HomePageViewModel =
                    viewModel(factory = HomePageViewModel.Factory)
                val uiState = homePageViewModel.uiState
                val searchViewModel: SearchScreenViewModel = viewModel()

                AppRoute.GenerateRoute(
                    navController = navController,
                    uiState = uiState,
                    homePageViewModel = homePageViewModel,
                    searchViewModel = searchViewModel
                )
            }
        }
        composeTestRule.waitForIdle()
        // Kiểm tra màn hình Home hiển thị
        composeTestRule.onNodeWithText("Trang chủ").assertExists()
    }

    @Test
    fun testNavigateToLogin() {
        composeTestRule.setContent {
            /// from Accompanist, setting a status bar
            val systemUiController = rememberSystemUiController()
            systemUiController.setStatusBarColor(
                color = Color.Transparent,
                darkIcons = true,
            )
            systemUiController.setNavigationBarColor(
                color = Color.Transparent,
                darkIcons = true,
            )


            AppTheme {
                /// main navigation
                val navController = rememberNavController()
                val homePageViewModel: HomePageViewModel =
                    viewModel(factory = HomePageViewModel.Factory)
                val uiState = homePageViewModel.uiState
                val searchViewModel: SearchScreenViewModel = viewModel()

                AppRoute.GenerateRoute(
                    navController = navController,
                    uiState = uiState,
                    homePageViewModel = homePageViewModel,
                    searchViewModel = searchViewModel
                )
            }
        }
    }

            @Test
            fun testNavigateToSearchWithCategory() {
                composeTestRule.setContent {
                    /// from Accompanist, setting a status bar
                    val systemUiController = rememberSystemUiController()
                    systemUiController.setStatusBarColor(
                        color = Color.Transparent,
                        darkIcons = true,
                    )
                    systemUiController.setNavigationBarColor(
                        color = Color.Transparent,
                        darkIcons = true,
                    )


                    AppTheme {
                        /// main navigation
                        val navController = rememberNavController()
                        val homePageViewModel: HomePageViewModel =
                            viewModel(factory = HomePageViewModel.Factory)
                        val uiState = homePageViewModel.uiState
                        val searchViewModel: SearchScreenViewModel = viewModel()

                        AppRoute.GenerateRoute(
                            navController = navController,
                            uiState = uiState,
                            homePageViewModel = homePageViewModel,
                            searchViewModel = searchViewModel
                        )
                    }
                    composeTestRule.onNodeWithContentDescription("Button 1").assertExists()
                    composeTestRule.onNodeWithText("Tìm kiếm").assertExists()
                }

            }
        }