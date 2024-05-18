package com.example.moviedbapp.data.network.dto.movieDetail

import com.example.moviedbapp.presentation.models.Movie

data class MovieDetailResponseDto(
    val adult: Boolean,
    val backdrop_path: String,
    val belongs_to_collection: BelongsToCollection,
    val budget: Long,
    val genres: List<Genre>,
    val homepage: String,
    val id: Int,
    val imdb_id: String,
    val origin_country: List<String>,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val production_companies: List<ProductionCompany>,
    val production_countries: List<ProductionCountry>,
    val release_date: String,
    val revenue: Long,
    val runtime: Int,
    val spoken_languages: List<SpokenLanguage>,
    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)
fun MovieDetailResponseDto.toDomain(): Movie {
    return Movie(
        backDropPath = backdrop_path,
        collection = belongs_to_collection,
        budget = budget,
        id = id,
        overview = overview,
        popularity = popularity,
        posterPath = poster_path,
        productionCompanies = production_companies,
        realeseDate = release_date,
        title = title,
        runtime = runtime,
        tagline = tagline,
        spoken_languages = spoken_languages,
        origin_country = origin_country,
        original_language = original_language
    )
}