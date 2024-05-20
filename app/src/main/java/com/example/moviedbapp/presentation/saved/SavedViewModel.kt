package com.example.moviedbapp.presentation.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedbapp.data.network.local.entities.MovieEntity
import com.example.moviedbapp.domain.useCases.GetSavedMoviesUseCase
import com.example.moviedbapp.presentation.saved.model.SavedViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(private val getSavedMoviesUseCase: GetSavedMoviesUseCase) : ViewModel() {
    var state = MutableStateFlow<SavedViewModelState>(SavedViewModelState.Loading)
        private set
    var movies = MutableStateFlow<List<MovieEntity>>(emptyList())
        private set

    fun initViewModel() {
        state.value = SavedViewModelState.Loading
        getMovies()
        state.value = SavedViewModelState.Success
    }

    private fun getMovies() {
        viewModelScope.launch {
            getSavedMoviesUseCase().collect {
                movies.value = it
            }
        }
    }
}