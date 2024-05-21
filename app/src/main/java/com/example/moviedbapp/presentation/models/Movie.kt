package com.example.moviedbapp.presentation.models

import com.example.moviedbapp.data.network.dto.movieDetail.BelongsToCollection
import com.example.moviedbapp.data.network.dto.movieDetail.ProductionCompany
import com.example.moviedbapp.data.network.dto.movieDetail.SpokenLanguage
import com.example.moviedbapp.data.network.local.entities.MovieEntity

data class Movie(
    val backDropPath: String? = "",
    val collection: BelongsToCollection? = BelongsToCollection("", 0, "", ""),
    val budget: Long = 0,
    val id: Int = 0,
    val overview: String = "",
    val popularity: Double = 0.1,
    val posterPath: String? = "",
    val productionCompanies: List<ProductionCompany> = emptyList(),
    val realeseDate: String = "",
    val title: String = "",
    val runtime: Int = 0,
    val tagline: String = "",
    val spoken_languages: List<SpokenLanguage> = emptyList(),
    val origin_country: List<String> = emptyList(),
    val original_language: String = "",
    var isSaved: Boolean = false
)
fun Movie.toEntity() =
    MovieEntity(
        id = id,
        poster_path = posterPath,
        title = title,
        backDropPath = backDropPath,
        budget = budget,
        overview = overview,
        popularity = popularity,
        realeseDate = realeseDate,
        runtime = runtime,
        tagline = tagline,
        original_language = original_language,
        isSaved = isSaved
    )
