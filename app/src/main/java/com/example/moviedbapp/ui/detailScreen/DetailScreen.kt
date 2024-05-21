package com.example.moviedbapp.ui.detailScreen

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviedbapp.data.network.dto.listResponse.toDomain
import com.example.moviedbapp.presentation.detail.DetailViewModel
import com.example.moviedbapp.presentation.detail.model.DetailViewModelState
import com.example.moviedbapp.presentation.models.MovieItem
import com.example.moviedbapp.ui.homeScreen.MovieItem
import com.example.moviedbapp.utils.Constants
import com.example.moviedbapp.utils.DateFormatter
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import java.io.IOException

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    detailViewModel: DetailViewModel,
    movieId: Int,
    offlineMode: Boolean
) {
    LaunchedEffect(movieId) {
        detailViewModel.initViewModel(movieId, offlineMode)
    }
    val state by detailViewModel.state.collectAsState()
    when (state) {
        is DetailViewModelState.Error -> {
            val errorState = state as DetailViewModelState.Error
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = "Error") },
                        navigationIcon = {
                            IconButton(
                                onClick = { navController.popBackStack() }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                }
            ) { pad ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(pad),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text =
                        when (errorState.exc) {
                            is IOException -> "A network error happened, please check your internet connection"
                            is HttpException -> "A server error happened, please try again later"
                            else -> "An error occurred, please try again later"
                        },
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { detailViewModel.initViewModel(movieId, offlineMode) }) {
                        Text(text = "Try Again")
                    }
                }
            }

        }

        is DetailViewModelState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is DetailViewModelState.Success -> {
            val successState = state as DetailViewModelState.Success
            val movie = successState.movie
            var isSaved by remember { mutableStateOf(movie.isSaved) }
            val relatedMovies by detailViewModel.relatedMovies.collectAsState()
            val collection by detailViewModel.collectionMovies.collectAsState()
            val gradientBrush = Brush.verticalGradient(
                colors = listOf(Color.Black, Color.Transparent),
            )
            val containerScrollState = rememberLazyListState()
            val firstVisibleItemIndex by remember { derivedStateOf { containerScrollState.firstVisibleItemIndex } }
            val tColor =
                if (firstVisibleItemIndex < 1) Color.Transparent else MaterialTheme.colorScheme.background
            val containerColor by animateColorAsState(targetValue = tColor, label = "")
            var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
            var buttonBackEnabled by rememberSaveable { mutableStateOf(true) }
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            if (firstVisibleItemIndex >= 1) {
                                Text(text = movie.title)
                            } else Text(
                                text = ""
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    buttonBackEnabled = false
                                    navController.popBackStack()
                                },
                                colors = IconButtonDefaults.iconButtonColors(
                                    contentColor = if (firstVisibleItemIndex < 1)
                                        Color.White else MaterialTheme.colorScheme.onBackground
                                ),
                                enabled = buttonBackEnabled
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBackIos,
                                    contentDescription = null
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = containerColor
                        ),
                        actions = {
                            if (isSaved) {
                                IconButton(
                                    colors = IconButtonDefaults.iconButtonColors(
                                        contentColor = if (firstVisibleItemIndex < 1)
                                            Color.White else MaterialTheme.colorScheme.onBackground
                                    ),
                                    onClick = {
                                    detailViewModel.deleteMovie(movie)
                                    isSaved = false
                                    if (offlineMode) navController.popBackStack()
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.BookmarkAdded,
                                        contentDescription = null
                                    )
                                }
                            } else {
                                IconButton(onClick = {
                                    detailViewModel.saveMovie(movie)
                                    isSaved = true
                                }) {
                                    Icon(
                                        imageVector = Icons.Outlined.BookmarkAdd,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    )
                }
            ) { _ ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    state = containerScrollState
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {

                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(Constants.IMAGE_BASE_URL + movie.posterPath)
                                    .crossfade(true)
                                    .dispatcher(Dispatchers.IO)
                                    .build(),
                                contentDescription = null,
                                contentScale = ContentScale.FillWidth,
                                filterQuality = FilterQuality.Low,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                                    .background(gradientBrush)
                            )
                        }
                    }

                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(y = (-20).dp)
                                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                                .background(MaterialTheme.colorScheme.background)
                                .padding(top = 30.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Info(title = "Realese Date", content = movie.realeseDate)
                                Spacer(
                                    modifier = Modifier
                                        .height(50.dp)
                                        .width(1.dp)
                                        .background(MaterialTheme.colorScheme.outlineVariant)
                                )
                                Info(title = "Popularity", content = movie.popularity.toString())
                                Spacer(
                                    modifier = Modifier
                                        .height(50.dp)
                                        .width(1.dp)
                                        .background(MaterialTheme.colorScheme.outlineVariant)
                                )
                                val formattedBudget = DateFormatter(movie.budget)
                                Info(title = "Budget (USD)", content = "$$formattedBudget")
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = movie.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = movie.overview,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            if (!offlineMode) {
                                if (movie.collection == null && relatedMovies.movieList.isNotEmpty()) selectedTabIndex =
                                    0
                                else if (movie.collection != null && relatedMovies.movieList.isEmpty()) selectedTabIndex =
                                    1
                                if (movie.collection != null || relatedMovies.movieList.isNotEmpty()) {
                                    TabRow(selectedTabIndex = selectedTabIndex) {
                                        if (relatedMovies.movieList.isNotEmpty())
                                            Tab(
                                                selected = selectedTabIndex == 0,
                                                onClick = { selectedTabIndex = 0 },
                                                text = { Text(text = "Similar Movies") }
                                            )
                                        if (movie.collection != null) {
                                            Tab(
                                                selected = selectedTabIndex == 1,
                                                onClick = { selectedTabIndex = 1 },
                                                text = { Text(text = "Collection") }
                                            )
                                        }

                                    }
                                    if (selectedTabIndex == 0) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        MoviesGrid(moviesList = relatedMovies.movieList.map { it.toDomain() }) {
                                            navController.navigate("movie/${it.id}")
                                        }
                                    }
                                    if (selectedTabIndex == 1) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        MoviesGrid(moviesList = collection.parts) {
                                            navController.navigate("movie/${it.id}")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun MoviesGrid(moviesList: List<MovieItem>, onClickMovie: (MovieItem) -> Unit) {
    val cols =
        if (moviesList.size % 3 == 0) moviesList.size / 3 else (moviesList.size / 3) + 1
    val height = cols * 175
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .height(height.dp)
            .padding(horizontal = 4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)

    ) {
        items(moviesList) { movie ->
            MovieItem(movie = movie, width = 60, height = 170) {
                onClickMovie(it)
            }
        }
    }
}

@Composable
fun Info(title: String, content: String) {
    Column {
        Text(
            text = title,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = content,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
    }
}


