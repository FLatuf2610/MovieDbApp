package com.example.moviedbapp.presentation.saved.model

import com.example.moviedbapp.presentation.saved.SavedViewModel

sealed class SavedViewModelState {
    object Loading: SavedViewModelState()
    data class Error(val exc: Exception): SavedViewModelState()
    object Success: SavedViewModelState()
}