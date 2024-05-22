package com.example.moviedbapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedbapp.domain.useCases.GetNowPlayingMoviesUseCase
import com.example.moviedbapp.domain.useCases.GetPopularMoviesUseCase
import com.example.moviedbapp.domain.useCases.GetTopRatedMovies
import com.example.moviedbapp.domain.useCases.GetUpComingMoviesUseCase
import com.example.moviedbapp.presentation.home.model.HomeViewModelState
import com.example.moviedbapp.presentation.models.MovieList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTopRatedMovies: GetTopRatedMovies,
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


    private suspend fun getTopMovies(page: Int) {
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

}
