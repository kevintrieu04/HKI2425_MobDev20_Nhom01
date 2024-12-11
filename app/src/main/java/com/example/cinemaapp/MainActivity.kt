package com.example.cinemaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.cinemaapp.ui.navigation.AppRoute
import com.example.cinemaapp.viewmodels.HomePageViewModel
import com.example.cinemaapp.viewmodels.SearchScreenViewModel
import com.example.compose.AppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
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
                val homePageViewModel: HomePageViewModel = viewModel(factory = HomePageViewModel.Factory)
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
}