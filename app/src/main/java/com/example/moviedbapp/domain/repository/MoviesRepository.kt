package com.example.moviedbapp.domain.repository

import com.example.moviedbapp.data.network.dto.collection.CollectionResponseDto
import com.example.moviedbapp.data.network.dto.listResponse.ListResponseDto
import com.example.moviedbapp.data.network.dto.movieDetail.MovieDetailResponseDto
import com.example.moviedbapp.data.local.entities.MovieEntity
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    suspend fun getPopularMovies(page: Int): ListResponseDto

    suspend fun getMovieById(id: Int): MovieDetailResponseDto

    suspend fun getRelatedMovies(movieId: Int, page: Int): ListResponseDto

    suspend fun getTopRatedMovies(page: Int): ListResponseDto

    suspend fun getNowPlayingMovies(page: Int): ListResponseDto

    suspend fun searchMovie(query: String, page: Int): ListResponseDto

    suspend fun getIncomingMovies(page: Int): ListResponseDto

    suspend fun getCollection(id: Int): CollectionResponseDto

    suspend fun getSavedMovieById(id: Int): MovieEntity?

    suspend fun saveMovie(movieEntity: MovieEntity)

    suspend fun getAllMoviesFromDb(): Flow<List<MovieEntity>>

    suspend fun deleteMovie(movieEntity: MovieEntity)
}