package com.example.moviedbapp.presentation.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedbapp.domain.useCases.GetSavedMoviesUseCase
import com.example.moviedbapp.presentation.models.MovieItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(private val getSavedMoviesUseCase: GetSavedMoviesUseCase) :
    ViewModel() {
    var movies = MutableStateFlow<List<MovieItem>>(emptyList())
        private set

    fun initViewModel() {
        getMovies()
    }

    private fun getMovies() {
        viewModelScope.launch {
            getSavedMoviesUseCase()
                .distinctUntilChanged()
                .catch { movies.value = emptyList() }
                .collect {
                    movies.value = it
                }
        }
    }
}
