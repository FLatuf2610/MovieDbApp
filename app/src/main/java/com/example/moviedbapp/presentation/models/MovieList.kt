package com.example.moviedbapp.presentation.models

import com.example.moviedbapp.data.network.dto.listResponse.Result

data class MovieList(
    val page: Int = 0,
    val movieList: List<Result> = emptyList()
)
