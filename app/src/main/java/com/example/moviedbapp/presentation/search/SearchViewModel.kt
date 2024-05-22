package com.example.moviedbapp.presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedbapp.domain.useCases.GetTrendingMoviesUseCase
import com.example.moviedbapp.domain.useCases.SearchMovieUseCase
import com.example.moviedbapp.presentation.models.MovieList
import com.example.moviedbapp.presentation.search.model.SearchViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase,
    private val searchMovieUseCase: SearchMovieUseCase
) : ViewModel() {

    private var isInitialized = false

    var searchedMovies = MutableStateFlow(MovieList())
        private set
    var state = MutableStateFlow<SearchViewModelState>(SearchViewModelState.Loading)
        private set

    var trendingMovies = MutableStateFlow(MovieList())
        private set

    private var searchJob: Job? = null

    var queryString by mutableStateOf("")

    fun initViewModel() {
        if (!isInitialized) {
            state.value = SearchViewModelState.Loading
            viewModelScope.launch {
                try {
                    trendingMovies.value = getTrendingMoviesUseCase()
                    state.value = SearchViewModelState.Success
                    isInitialized = true
                } catch (e: Exception) {
                    state.value = SearchViewModelState.Error(exc = e)
                }
            }
        }
        else return
    }

    fun searchMovie(page: Int) {
        if (queryString.isBlank()) {
            searchedMovies.value = MovieList()
            return
        }
        state.value = SearchViewModelState.Loading
        searchJob = null
        searchJob = viewModelScope.launch {
            delay(500)
            try {
                val response = searchMovieUseCase(queryString, page)
                if (page == 1) {
                    searchedMovies.value = response
                }
                else {
                    val auxList = searchedMovies.value.movieList.toMutableList()
                    auxList.addAll(response.movieList)
                    val newState = searchedMovies.value.copy(page = page, movieList = auxList)
                    searchedMovies.value = newState
                }
                state.value = SearchViewModelState.Success
            } catch (e: Exception) {
                state.value = SearchViewModelState.Error(exc = e)
            }
        }
    }

    fun clearInput() {
        queryString = ""
        searchedMovies.value = MovieList()
    }
}