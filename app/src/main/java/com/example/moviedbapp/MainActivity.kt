package com.example.moviedbapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.moviedbapp.presentation.detail.DetailViewModel
import com.example.moviedbapp.presentation.home.HomeViewModel
import com.example.moviedbapp.presentation.saved.SavedViewModel
import com.example.moviedbapp.ui.detailScreen.DetailScreen
import com.example.moviedbapp.ui.homeScreen.HomeScreen
import com.example.moviedbapp.ui.homeScreen.SearchScreen
import com.example.moviedbapp.ui.savedScreen.SavedScreen
import com.example.moviedbapp.ui.theme.MovieDbAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val homeViewModel: HomeViewModel = hiltViewModel()
            val detailViewModel: DetailViewModel = hiltViewModel()
            val savedViewModel: SavedViewModel = hiltViewModel()
            val navController = rememberNavController()
            MovieDbAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            HomeScreen(navController = navController, viewModel = homeViewModel)
                        }
                        composable(
                            "movie/{movieId}?offlineMode={offlineMode}",
                            arguments = listOf(
                                navArgument("movieId") { type = NavType.IntType },
                                navArgument("offlineMode") { defaultValue = false }
                                )
                        ) {
                            val movieId = it.arguments?.getInt("movieId")!!
                            val offlineMode = it.arguments?.getBoolean("offlineMode")
                            DetailScreen(
                                navController = navController,
                                detailViewModel = detailViewModel,
                                movieId = movieId,
                                offlineMode = offlineMode!!
                            )
                        }
                        composable("search") {
                            SearchScreen(viewModel = homeViewModel, navController)
                        }
                        composable("saved") {
                            SavedScreen(viewModel = savedViewModel, navController = navController)
                        }
                    }
                }
            }
        }
    }
}



