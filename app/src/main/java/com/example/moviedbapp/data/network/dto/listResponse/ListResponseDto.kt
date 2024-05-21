package com.example.moviedbapp.data.network.dto.listResponse

import com.example.moviedbapp.presentation.models.MovieList
import com.google.gson.annotations.SerializedName

data class ListResponseDto(
    val page: Int,
    val results: List<Result>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)
fun ListResponseDto.toDomain(): MovieList {
    return MovieList(page = page, movieList = results)
}
