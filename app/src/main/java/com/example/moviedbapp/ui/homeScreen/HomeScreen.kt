package com.example.moviedbapp.ui.homeScreen

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviedbapp.data.network.dto.listResponse.toDomain
import com.example.moviedbapp.presentation.home.HomeViewModel
import com.example.moviedbapp.presentation.home.model.HomeViewModelState
import com.example.moviedbapp.presentation.models.MovieItem
import com.example.moviedbapp.presentation.models.MovieList
import com.example.moviedbapp.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel) {
    LaunchedEffect(Unit) {
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
                        Text(text = "Or")
                        Button(onClick = { navController.navigate("saved") }) {
                            Text(text = "Go to Saved Movies")
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
                            IconButton(onClick = { navController.navigate("saved") }) {
                                Icon(imageVector = Icons.Filled.BookmarkAdded, contentDescription = null)
                            }
                        }
                    )
                }
            ) { pad ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = pad.calculateTopPadding())
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        item {
                            val pagerState = rememberPagerState {
                                topMovies.movieList.size
                            }
                            TopMoviesList(movies = topMovies, pagerState = pagerState) {
                                navController.navigate("movie/$it")
                            }
                        }

                        item {
                            MoviesList(
                                titleSection = "Most Popular Movies",
                                list = popularMovies,
                                isLoading = viewModel.popularMoviesLoading,
                                onFinishScroll = {
                                    scope.launch {
                                        viewModel.getPopularMovies(it)
                                    }
                                }) { movie ->
                                navController.navigate("movie/${movie.id}")
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            MoviesList(
                                titleSection = "Movies in Theatre",
                                list = nowMovies,
                                isLoading = viewModel.nowMoviesLoading,
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
                                isLoading = viewModel.upComingMoviesLoading,
                                onFinishScroll = {
                                    scope.launch {
                                        viewModel.getUpComingMovies(it)
                                    }
                                }) { movie ->
                                navController.navigate("movie/${movie.id}")
                            }
                            Spacer(modifier = Modifier.height(32.dp))
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
    isLoading: Boolean,
    onFinishScroll: (page: Int) -> Unit,
    onClickMovie: (MovieItem) -> Unit
) {
    val lazyRowState = rememberLazyListState()
    Text(
        text = titleSection,
        fontSize = 16.sp,
        modifier = Modifier.padding(horizontal = 16.dp),
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(16.dp))
    LazyRow(
        state = lazyRowState,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        stickyHeader {
            Spacer(modifier = Modifier.width(8.dp))
        }
        itemsIndexed(list.movieList.map { it.toDomain() }) { index, movie ->
            MovieItem(movie = movie) {
                onClickMovie(it)
            }
            if (index == list.movieList.lastIndex)
                LaunchedEffect(Unit) {
                    val newPage = list.page + 1
                    onFinishScroll(newPage)
                    Log.i("FINAL DEL SCROLL", newPage.toString())
                }
        }
        if (isLoading && list.movieList.isNotEmpty()) {
            item { CircularProgressIndicator() }
        }
    }
}


@Composable
fun MovieItem(movie: MovieItem, width: Int = 112, height: Int = 160, onClickMovie: (MovieItem) -> Unit) {
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
                .size(240)
                .crossfade(250)
                .dispatcher(Dispatchers.IO)
                .build(),
            contentScale = ContentScale.Crop,
            placeholder = null,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            filterQuality = FilterQuality.Low
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopMoviesList(movies: MovieList, pagerState: PagerState, onClick: (Int) -> Unit) {
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = "Top Rated Movies",
        modifier = Modifier.padding(horizontal = 16.dp),
        fontWeight = FontWeight.SemiBold,
    )
    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 40.dp, vertical = 20.dp),
    ) {
        val movie = movies.movieList[it]
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(horizontal = 8.dp)
                    .clickable { onClick(movie.id) }
                    .focusable()
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(Constants.IMAGE_BASE_URL + movie.posterPath)
                        .crossfade(250)
                        .size(480)
                        .dispatcher(Dispatchers.IO)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    placeholder = null,
                    modifier = Modifier
                        .fillMaxWidth(),
                    filterQuality = FilterQuality.Low
                    )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(
                    text = movie.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Button(onClick = { onClick(movie.id) }) {
                Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Go To Movie")
            }
        }
    }
}










