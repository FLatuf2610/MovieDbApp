package com.example.moviedbapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moviedbapp.presentation.models.Movie
import com.example.moviedbapp.presentation.models.MovieItem

@Entity(tableName = "movie_entity")
data class MovieEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val posterPath: String?,
    val title: String,
    val backDropPath: String?,
    val budget: Long,
    val overview: String,
    val popularity: Double,
    val realeseDate: String,
    val runtime: Int,
    val tagline: String,
    val originalLanguage: String,
    var isSaved: Boolean
)
fun MovieEntity.toDomain(): Movie =
    Movie(
        id = id,
        backDropPath = backDropPath,
        budget = budget,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        realeseDate = realeseDate,
        title = title,
        runtime = runtime,
        tagline = tagline,
        originalLanguage =  originalLanguage,
        isSaved = isSaved
    )
fun MovieEntity.toListItem() =
    MovieItem(posterPath, id, title)
