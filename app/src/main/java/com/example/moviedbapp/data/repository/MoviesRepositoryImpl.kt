package com.example.moviedbapp.data.repository

import com.example.moviedbapp.data.network.apiClient.MovieDbApiClient
import com.example.moviedbapp.data.network.dto.collection.CollectionResponseDto
import com.example.moviedbapp.data.network.dto.listResponse.ListResponseDto
import com.example.moviedbapp.data.network.dto.movieDetail.MovieDetailResponseDto
import com.example.moviedbapp.data.network.local.dao.MoviesDao
import com.example.moviedbapp.data.network.local.entities.MovieEntity
import com.example.moviedbapp.domain.repository.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepositoryImpl @Inject constructor(
    private val apiClient: MovieDbApiClient,
    private val moviesDao: MoviesDao
) : MoviesRepository {

    override suspend fun getPopularMovies(page: Int): ListResponseDto {
        return withContext(Dispatchers.IO) {
            apiClient.getPopularMovies(page)
        }
    }

    override suspend fun getMovieById(id: Int): MovieDetailResponseDto {
        return withContext(Dispatchers.IO) {
            apiClient.getMovieById(id)
        }
    }

    override suspend fun getRelatedMovies(movieId: Int, page: Int): ListResponseDto {
        return withContext(Dispatchers.IO) {
            apiClient.getRelatedMovies(movieId, page)
        }
    }

    override suspend fun getTopRatedMovies(page: Int): ListResponseDto {
        return withContext(Dispatchers.IO) {
            apiClient.getTopRatedMovies(page)
        }
    }

    override suspend fun getNowPlayingMovies(page: Int): ListResponseDto {
        return withContext(Dispatchers.IO) {
            apiClient.getNowPlaying(page)
        }
    }

    override suspend fun searchMovie(query: String, page: Int): ListResponseDto {
        return withContext(Dispatchers.IO) {
            apiClient.searchMovie(query, page)
        }
    }

    override suspend fun getIncomingMovies(page: Int): ListResponseDto {
        return withContext(Dispatchers.IO) {
            apiClient.getIncomingMovies(page)
        }
    }

    override suspend fun getCollection(id: Int): CollectionResponseDto {
        return withContext(Dispatchers.IO) {
            apiClient.getCollection(id)
        }
    }

    override suspend fun getSavedMovieById(id: Int): MovieEntity? {
        return withContext(Dispatchers.IO) {
            moviesDao.getSavedMovieById(id)
        }
    }

    override suspend fun saveMovie(movieEntity: MovieEntity) {
        return withContext(Dispatchers.IO) {
            moviesDao.saveMovie(movieEntity)
        }
    }

    override suspend fun getAllMoviesFromDb(): Flow<List<MovieEntity>> {
        return withContext(Dispatchers.IO) {
            moviesDao.getSavedMovies()
        }
    }

    override suspend fun deleteMovie(movieEntity: MovieEntity) {
        return withContext(Dispatchers.IO) {
            moviesDao.deleteMovie(movieEntity)
        }
    }


}