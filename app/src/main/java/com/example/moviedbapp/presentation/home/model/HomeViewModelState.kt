package com.example.moviedbapp.presentation.home.model

sealed class HomeViewModelState {
    data object Loading: HomeViewModelState()
    data object Success: HomeViewModelState()
    data class Error(val exc: Exception): HomeViewModelState()
}