package com.example.moviedbapp.presentation.search.model

import java.lang.Exception

sealed class SearchViewModelState {

    data object Loading: SearchViewModelState()

    data object Success: SearchViewModelState()

    data class Error(val exc: Exception): SearchViewModelState()
}