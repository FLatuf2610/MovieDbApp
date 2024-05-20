package com.example.moviedbapp.presentation.models

import com.example.moviedbapp.data.network.dto.movieDetail.BelongsToCollection
import com.example.moviedbapp.data.network.dto.movieDetail.ProductionCompany
import com.example.moviedbapp.data.network.dto.movieDetail.SpokenLanguage

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
