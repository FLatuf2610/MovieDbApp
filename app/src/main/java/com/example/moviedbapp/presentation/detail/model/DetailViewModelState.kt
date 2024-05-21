package com.example.moviedbapp.presentation.detail.model

import com.example.moviedbapp.presentation.models.Movie

sealed class DetailViewModelState {
    data object Loading: DetailViewModelState()
    data class Success(val movie: Movie): DetailViewModelState()
    data class Error(val exc: Exception): DetailViewModelState()
}