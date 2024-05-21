package com.example.moviedbapp.data.network.dto.collection

import com.example.moviedbapp.presentation.models.Collection
import com.google.gson.annotations.SerializedName

data class CollectionResponseDto(
    @SerializedName("backdrop_path")
    val backdropPath: String,
    val id: Int,
    val name: String,
    val overview: String,
    val parts: List<Part>,
    @SerializedName("poster_path")
    val posterPath: String
)
fun CollectionResponseDto.toDomain() :Collection {
    return Collection(
        parts.map { it.toDomain() }
    )
}
