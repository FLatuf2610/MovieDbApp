package com.example.moviedbapp.presentation.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedbapp.domain.useCases.GetCollectionUseCase
import com.example.moviedbapp.domain.useCases.GetMovieDetailUseCase
import com.example.moviedbapp.domain.useCases.GetRelatedMoviesUseCase
import com.example.moviedbapp.presentation.detail.model.DetailViewModelState
import com.example.moviedbapp.presentation.detail.model.DetailViewModelState.Success
import com.example.moviedbapp.presentation.detail.model.DetailViewModelState.Error
import com.example.moviedbapp.presentation.detail.model.DetailViewModelState.Loading
import com.example.moviedbapp.presentation.models.Collection
import com.example.moviedbapp.presentation.models.Movie
import com.example.moviedbapp.presentation.models.MovieList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val getRelatedMoviesUseCase: GetRelatedMoviesUseCase,
    private val getCollectionUseCase: GetCollectionUseCase
) : ViewModel() {

    var state = MutableStateFlow<DetailViewModelState>(Success(Movie()))
        private set

    var relatedMovies = MutableStateFlow(MovieList())
        private set

    var collectionMovies = MutableStateFlow(Collection())
        private set


    fun initViewModel(movieId: Int) {
        viewModelScope.launch {
            state.value = Loading
            try {
                val movie = async { getMovieDetailUseCase(movieId) }.await()
                getRelatedMovies(movieId)
                if (movie.collection != null) getCollection(movie.collection.id)
                state.value = Success(movie)
            } catch (e: Exception) {
                state.value = Error(e)
            }
        }
    }

    private fun getRelatedMovies(movieId: Int) {
        viewModelScope.launch {
            try {
                val movies = getRelatedMoviesUseCase(movieId, 1)
                relatedMovies.value = movies
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getCollection(collectionId: Int) {
        viewModelScope.launch {
            try {
                val collection = getCollectionUseCase(collectionId)
                collectionMovies.value = collection
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

}