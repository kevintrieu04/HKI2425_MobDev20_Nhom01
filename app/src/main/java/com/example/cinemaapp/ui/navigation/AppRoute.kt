package com.example.cinemaapp.ui.navigation

import SearchSceen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cinemaapp.network.LoginManager
import com.example.cinemaapp.ui.DetailScreen
import com.example.cinemaapp.ui.HomeScreen
import com.example.cinemaapp.ui.LoginPage
import com.example.cinemaapp.ui.SeatSelectorScreen
import com.example.cinemaapp.viewmodels.HomePageUiState
import com.example.cinemaapp.viewmodels.HomePageViewModel
import com.example.cinemaapp.ui.SignUpScreen
object AppRoute {

    @Composable
    fun GenerateRoute(navController: NavHostController,
                      uiState: HomePageUiState,
                      viewModel: HomePageViewModel
    ) {
        NavHost(
            navController = navController,
            startDestination = AppRouteName.Home
        ) {
            composable(AppRouteName.Home) {
                HomeScreen(navController = navController, uiState = uiState, viewModel = viewModel)
            }
            composable("${AppRouteName.Detail}/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                if (uiState is HomePageUiState.Success) {
                    val movie = uiState.movies.first { it.id == id }
                    DetailScreen(navController = navController, movie)
                }
            }
            composable(AppRouteName.SeatSelector) {
                SeatSelectorScreen(navController = navController)
            }
            composable(AppRouteName.Login) {
                LoginPage(navController = navController)
            }
            composable(AppRouteName.Register) {
                SignUpScreen(navController = navController)
            }
            composable("${AppRouteName.Drawer}/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                if (id == "profile") {
                    /* TODO */
                } else if (id == "search") {
                    SearchSceen()
                }
            }
        }
    }
}
