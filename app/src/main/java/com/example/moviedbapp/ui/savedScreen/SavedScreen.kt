package com.example.moviedbapp.ui.savedScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.moviedbapp.presentation.saved.SavedViewModel
import com.example.moviedbapp.ui.searchScreen.SearchedMovieItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedScreen(viewModel: SavedViewModel, navController: NavController) {

    val movies by viewModel.movies.collectAsState()
    var backButtonEnabled by remember { mutableStateOf(true) }
    LaunchedEffect(key1 = Unit) {
        viewModel.initViewModel()
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Saved Movies") },
                navigationIcon = {
                    IconButton(
                        enabled = backButtonEnabled,
                        onClick = {
                            navController.popBackStack()
                            backButtonEnabled = false
                        }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBackIos, contentDescription = null)
                    }
                }
            )
        }
    ) { pad ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(pad)
        ) {
            if (movies.isEmpty()) {
                Text(text = "You have no saved movies", modifier = Modifier.align(Alignment.Center))
            }
            LazyColumn {
                itemsIndexed(movies) { _, item ->
                    SearchedMovieItem(movie = item) {
                        navController.navigate("movie/${it.id}?offlineMode=${true}")
                    }
                }
            }
        }
    }
}