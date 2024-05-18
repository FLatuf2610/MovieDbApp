package com.example.moviedbapp.presentation.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedbapp.domain.useCases.GetNowPlayingMoviesUseCase
import com.example.moviedbapp.domain.useCases.GetPopularMoviesUseCase
import com.example.moviedbapp.domain.useCases.GetTopRatedMovies
import com.example.moviedbapp.domain.useCases.GetUpComingMoviesUseCase
import com.example.moviedbapp.domain.useCases.SearchMovieUseCase
import com.example.moviedbapp.presentation.models.MovieList
import com.example.moviedbapp.presentation.home.model.HomeViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTopRatedMovies: GetTopRatedMovies,
    private val searchMovieUseCase: SearchMovieUseCase,
    private val getUpComingMoviesUseCase: GetUpComingMoviesUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<HomeViewModelState>(HomeViewModelState.Loading)
    val state: StateFlow<HomeViewModelState> = _state

    private val _topMovies = MutableStateFlow(MovieList())
    val topMovies: StateFlow<MovieList> = _topMovies

    private val _upComingMovies = MutableStateFlow(MovieList())
    val upComingMovies: StateFlow<MovieList> = _upComingMovies

    private val _popularMovies = MutableStateFlow(MovieList())
    val popularMovies: StateFlow<MovieList> = _popularMovies

    private val _nowMovies = MutableStateFlow(MovieList())
    val nowMovies: StateFlow<MovieList> = _nowMovies

    private val _searchedMovies = MutableStateFlow(MovieList())
    val searchedMovies: StateFlow<MovieList> =  _searchedMovies

    private var queryJob: Job? = null

    var queryString by mutableStateOf("")

    fun initViewModel() {
        viewModelScope.launch {
            _state.value = HomeViewModelState.Loading
            try {
                getTopMovies(1)
                getPopularMovies(1)
                getUpComingMovies(1)
                getNowMovies(1)
                _state.value = HomeViewModelState.Success
            } catch (e: Exception) {
                _state.value = HomeViewModelState.Error(e)
            }
        }
    }

    suspend fun getTopMovies(page: Int) {
        try {
            val topMovies = getTopRatedMovies(page)
            if (page == 1) {
                _topMovies.value = topMovies
            }
            else {
                val auxList = _topMovies.value.movieList.toMutableList()
                auxList.addAll(topMovies.movieList)
                auxList.toList()
                val newState = _topMovies.value.copy(page = page, movieList = auxList)
                _topMovies.value = newState
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getPopularMovies(page: Int) {
        try {
            val popularMovies = getPopularMoviesUseCase(page)
            if (page == 1) {
                _popularMovies.value = popularMovies
            }
            else {
                val auxList = _popularMovies.value.movieList.toMutableList()
                auxList.addAll(popularMovies.movieList)
                val newState = _popularMovies.value.copy(page = page, movieList = auxList)
                _popularMovies.value = newState
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getUpComingMovies(page: Int) {
        try {
            val upComingMovies= getUpComingMoviesUseCase(page)
            if (page == 1) {
                _upComingMovies.value = upComingMovies
            }
            else {
                val auxList = _upComingMovies.value.movieList.toMutableList()
                auxList.addAll(upComingMovies.movieList)
                val newState = _upComingMovies.value.copy(page = page, movieList = auxList)
                _upComingMovies.value = newState
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getNowMovies(page: Int) {
        try {
            val nowMovies = getNowPlayingMoviesUseCase(page)
            if (page == 1) {
                _nowMovies.value = nowMovies
            }
            else {
                val auxList = _nowMovies.value.movieList.toMutableList()
                auxList.addAll(nowMovies.movieList)
                val newState = _nowMovies.value.copy(page = page, movieList = auxList)
                _nowMovies.value = newState
            }
        } catch (e: Exception) {
            throw e
        }
    }

    fun searchMovie(page: Int) {
        queryJob = null

        queryJob = viewModelScope.launch {
            delay(500)
            try {
                val response = searchMovieUseCase(queryString, page)
                if (page == 1) {
                    _searchedMovies.value = response
                }
                else {
                    val auxList = _searchedMovies.value.movieList.toMutableList()
                    auxList.addAll(response.movieList)
                    val newState = _searchedMovies.value.copy(page = page, movieList = auxList)
                    _searchedMovies.value = newState
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("ERROR", e.message!!)
            }
        }
    }


}
