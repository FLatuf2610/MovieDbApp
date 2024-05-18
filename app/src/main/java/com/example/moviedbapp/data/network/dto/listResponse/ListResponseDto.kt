package com.example.moviedbapp.data.network.dto.listResponse

import com.example.moviedbapp.presentation.models.MovieList

data class ListResponseDto(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)
fun ListResponseDto.toDomain(): MovieList {
    return MovieList(page = page, movieList = results)
}
