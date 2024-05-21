package com.example.moviedbapp.data.network.dto.collection

import com.example.moviedbapp.presentation.models.MovieItem

data class Part(
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: List<Int>,
    val id: Int,
    val media_type: String,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)
fun Part.toDomain(): MovieItem {
    return MovieItem(
        posterPath = poster_path,
        id = id,
        title = title
    )
}