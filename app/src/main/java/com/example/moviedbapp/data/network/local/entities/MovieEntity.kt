package com.example.moviedbapp.data.network.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_entity")
data class MovieEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val poster_path: String?,
    val title: String?
)
