package com.example.moviedbapp.data.network.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moviedbapp.presentation.models.Movie
import com.example.moviedbapp.presentation.models.MovieItem

@Entity(tableName = "movie_entity")
data class MovieEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val poster_path: String?,
    val title: String,
    val backDropPath: String?,
    val budget: Long,
    val overview: String,
    val popularity: Double,
    val realeseDate: String,
    val runtime: Int,
    val tagline: String,
    val original_language: String,
    var isSaved: Boolean
)
fun MovieEntity.toDomain(): Movie =
    Movie(
        id = id,
        backDropPath = backDropPath,
        budget = budget,
        overview = overview,
        popularity = popularity,
        posterPath = poster_path,
        realeseDate = realeseDate,
        title = title,
        runtime = runtime,
        tagline = tagline,
        original_language = original_language,
        isSaved = isSaved
    )
fun MovieEntity.toListItem() =
    MovieItem(poster_path, id, title)
