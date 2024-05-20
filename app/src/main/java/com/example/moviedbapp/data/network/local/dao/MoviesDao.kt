package com.example.moviedbapp.data.network.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.moviedbapp.data.network.local.entities.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {
    @Query("SELECT * FROM movie_entity WHERE movie_entity.id == :id")
    fun getSavedMovieById(id: Int): MovieEntity?

    @Query("SELECT * FROM movie_entity")
    fun getSavedMovies(): Flow<List<MovieEntity>>

    @Insert
    suspend fun saveMovie(movieEntity: MovieEntity)

    @Delete
    suspend fun deleteMovie(movieEntity: MovieEntity)
}