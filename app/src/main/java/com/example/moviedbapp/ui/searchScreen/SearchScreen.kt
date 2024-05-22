package com.example.moviedbapp.ui.searchScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviedbapp.data.network.dto.listResponse.toDomain
import com.example.moviedbapp.presentation.models.MovieItem
import com.example.moviedbapp.presentation.search.SearchViewModel
import com.example.moviedbapp.presentation.search.model.SearchViewModelState
import com.example.moviedbapp.ui.homeScreen.MovieItem
import com.example.moviedbapp.utils.Constants
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SearchScreen(viewModel: SearchViewModel, navController: NavController) {
    val searchedMovies by viewModel.searchedMovies.collectAsState()
    val trendingMovies by viewModel.trendingMovies.collectAsState()
    val state by viewModel.state.collectAsState()
    val keyboardState = LocalSoftwareKeyboardController.current
    var backButtonEnabled by remember { mutableStateOf(true) }
    LaunchedEffect(key1 = Unit) {
        viewModel.initViewModel()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        SearchBar(
            query = viewModel.queryString,
            onQueryChange = {
                viewModel.queryString = it
                viewModel.searchMovie(1)

            },
            onSearch = {
                if (searchedMovies.movieList.isNotEmpty()) {
                    navController.navigate("movie/${searchedMovies.movieList[0].id}")
                }
            },
            active = true,
            onActiveChange = {},
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = { Text(text = "Search Movie") },
            leadingIcon = {
                IconButton(
                    enabled = backButtonEnabled,
                    onClick = {
                        backButtonEnabled = false
                        navController.popBackStack()
                        viewModel.clearInput()
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                        contentDescription = null
                    )
                }
            },
            colors = SearchBarDefaults.colors(
                containerColor = Color.Unspecified,

                ),
            trailingIcon = {
                IconButton(
                    onClick = {
                        navController.navigate("movie/${searchedMovies.movieList[0].id}")
                    },
                    enabled = searchedMovies.movieList.isNotEmpty()
                ) {
                    Icon(imageVector = Icons.Rounded.Search, contentDescription = null)
                }
            }
        ) {
            val lazyColumnState = rememberLazyListState()
            LaunchedEffect (lazyColumnState.isScrollInProgress) {
                keyboardState?.hide()
            }

            if (searchedMovies.movieList.isEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Trending movies",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    stickyHeader { Spacer(modifier = Modifier.width(8.dp)) }
                    items(trendingMovies.movieList) {
                        MovieItem(movie = it.toDomain(), width = 80, height = 124) { movieItem ->
                            navController.navigate("movie/${movieItem.id}")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Discover other title to watch",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,

                        )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Search your favourites movies and explore all our catalog",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                LazyColumn(
                    state = lazyColumnState,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    itemsIndexed(searchedMovies.movieList) { index, movie ->
                        SearchedMovieItem(movie = movie.toDomain()) {
                            navController.navigate("movie/${it.id}")
                            keyboardState?.hide()
                        }
                        if (index == searchedMovies.movieList.lastIndex - 5) {
                            LaunchedEffect(Unit) {
                                viewModel.searchMovie(searchedMovies.page + 1)
                            }
                        }
                    }
                    if (state == SearchViewModelState.Loading) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchedMovieItem(movie: MovieItem, onClickMovie: (MovieItem) -> Unit) {
    Surface(
        onClick = { onClickMovie(movie) },
        modifier = Modifier.fillMaxWidth(),
        color = Color.Unspecified
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier.size(80.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(Constants.IMAGE_BASE_URL + movie.posterPath)
                        .crossfade(250)
                        .size(240)
                        .dispatcher(Dispatchers.IO)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    filterQuality = FilterQuality.Low,
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                modifier = Modifier.weight(1f),
                text = movie.title,
                fontSize = 16.sp,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Outlined.ArrowForwardIos, null)
        }
    }
}

