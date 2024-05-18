package com.example.moviedbapp.ui.homeScreen

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviedbapp.data.network.dto.listResponse.Result
import com.example.moviedbapp.data.network.dto.listResponse.toDomain
import com.example.moviedbapp.presentation.models.MovieList
import com.example.moviedbapp.presentation.home.HomeViewModel
import com.example.moviedbapp.presentation.home.model.HomeViewModelState
import com.example.moviedbapp.presentation.models.MovieItem
import com.example.moviedbapp.utils.Constants
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel) {
    LaunchedEffect(true) {
        viewModel.initViewModel()
    }
    val state by viewModel.state.collectAsState()

    when (state) {
        is HomeViewModelState.Error -> {
            val errorState = state as HomeViewModelState.Error
            Scaffold(
                topBar = {
                    TopAppBar(title = {
                        Text(
                            text = "The MovieDB",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding()
                        )
                    })
                }
            ) { pad ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(pad),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = when (errorState.exc) {
                                is IOException -> "A network error happened, please check your internet connection"
                                is HttpException -> "A server error happened, please try again later"
                                else -> "An error occurred, please try again later"
                            },
                            textAlign = TextAlign.Center
                        )
                        Button(onClick = { viewModel.initViewModel() }) {
                            Text(text = "Try Again")
                        }
                    }
                }
            }
        }

        is HomeViewModelState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is HomeViewModelState.Success -> {
            val topMovies by viewModel.topMovies.collectAsState()
            val upComingMovies by viewModel.upComingMovies.collectAsState()
            val nowMovies by viewModel.nowMovies.collectAsState()
            val popularMovies by viewModel.popularMovies.collectAsState()
            val scope = rememberCoroutineScope()
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "The MovieDB",
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(8.dp)
                            )
                        },
                        actions = {
                            IconButton(onClick = { navController.navigate("search") }) {
                                Icon(imageVector = Icons.Default.Search, contentDescription = null)
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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        MoviesList(
                            titleSection = "Most Popular Movies",
                            list = popularMovies,
                            onFinishScroll = {
                                scope.launch {
                                    viewModel.getPopularMovies(it)
                                }
                            }) { movie ->
                            navController.navigate("movie/${movie.id}")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        MoviesList(
                            titleSection = "Top Rated Movies",
                            list = topMovies,
                            onFinishScroll = {
                                scope.launch {
                                    viewModel.getTopMovies(it)
                                }
                            }) { movie ->
                            navController.navigate("movie/${movie.id}")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        MoviesList(
                            titleSection = "Movies in Theatre",
                            list = nowMovies,
                            onFinishScroll = {
                                scope.launch {
                                    viewModel.getNowMovies(it)
                                }
                            }) { movie ->
                            navController.navigate("movie/${movie.id}")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        MoviesList(
                            titleSection = "Upcoming Movies",
                            list = upComingMovies,
                            onFinishScroll = {
                                scope.launch {
                                    viewModel.getUpComingMovies(it)
                                }
                            }) { movie ->
                            navController.navigate("movie/${movie.id}")
                        }
                    }
                }
            }

        }

    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MoviesList(
    titleSection: String,
    list: MovieList,
    onFinishScroll: (page: Int) -> Unit,
    onClickMovie: (MovieItem) -> Unit
) {
    val lazyRowState = rememberLazyListState()
    Text(
        text = titleSection,
        fontSize = 20.sp,
        modifier = Modifier.padding(horizontal = 16.dp),
        fontWeight = FontWeight.SemiBold
    )
    Spacer(modifier = Modifier.height(16.dp))
    LazyRow(
        state = lazyRowState,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        stickyHeader {
            Spacer(modifier = Modifier.width(8.dp))
        }
        itemsIndexed(list.movieList.map { it.toDomain() }) { index, movie ->
            MovieItem(movie = movie) {
                onClickMovie(it)
            }
            if (index == list.movieList.lastIndex - 3)
                LaunchedEffect(Unit) {
                    val newPage = list.page + 1
                    onFinishScroll(newPage)
                    Log.i("FINAL DEL SCROLL", newPage.toString())
                }
        }
    }
}


@Composable
fun MovieItem(movie: MovieItem, width: Int = 175, height: Int = 250, onClickMovie: (MovieItem) -> Unit) {
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClickMovie(movie) }
            .width(width.dp)
            .height(height.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(Constants.IMAGE_BASE_URL + movie.posterPath)
                .crossfade(true)
                .build(),
            contentScale = ContentScale.Crop,
            placeholder = null,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview
@Composable
fun MovieItemPreview() {
    Card(
        modifier = Modifier
            .height(275.dp)
            .width(200.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRPQ9lCyEVXExqW_a4e6yB4rf8hLuOhyrzyS2GdTux7LQ&s")
                .crossfade(true)
                .build(),
            contentScale = ContentScale.Crop,
            placeholder = null,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun SearchedMovieItem(movie: Result, onClickMovie: (Result) -> Unit) {
    Surface(
        onClick = { onClickMovie(movie) },
        modifier = Modifier.fillMaxWidth()
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
                    model = Constants.IMAGE_BASE_URL + movie.poster_path,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                modifier = Modifier.weight(1f),
                text = movie.title,
                fontSize = 24.sp,
            )
            Icon(Icons.Default.KeyboardArrowRight, null)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: HomeViewModel, navController: NavController) {
    val searchedMovies by viewModel.searchedMovies.collectAsState()
    val keyboardState = LocalSoftwareKeyboardController.current
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
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
                onActiveChange = { },
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = { Text(text = "Search Movie") },
                leadingIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            ) {
                val lazyColumnState = rememberLazyListState()
                if (lazyColumnState.isScrollInProgress) {
                    keyboardState?.hide()
                }
                LazyColumn(
                    state = lazyColumnState
                ) {
                    itemsIndexed(searchedMovies.movieList) { index, movie ->
                        SearchedMovieItem(movie = movie) {
                            navController.navigate("movie/${it.id}")
                            keyboardState?.hide()
                        }
                        if (index == searchedMovies.movieList.lastIndex - 5) {
                            LaunchedEffect(Unit) {
                                viewModel.searchMovie(searchedMovies.page + 1)
                                Log.i("TERMINA SCROLL", searchedMovies.page.toString())
                            }
                        }
                    }
                }
            }
        }
    }
}






