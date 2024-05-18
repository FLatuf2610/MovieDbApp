package com.example.moviedbapp.presentation.home.model

sealed class HomeViewModelState {
    object Loading: HomeViewModelState()
    object Success: HomeViewModelState()
    data class Error(val exc: Exception): HomeViewModelState()
}