package com.example.moviedbapp.data.network.dto.collection

import com.example.moviedbapp.presentation.models.Collection
import com.example.moviedbapp.presentation.models.MovieList

data class CollectionResponseDto(
    val backdrop_path: String,
    val id: Int,
    val name: String,
    val overview: String,
    val parts: List<Part>,
    val poster_path: String
)
fun CollectionResponseDto.toDomain() :Collection {
    return Collection(
        parts.map { it.toDomain() }
    )
}
