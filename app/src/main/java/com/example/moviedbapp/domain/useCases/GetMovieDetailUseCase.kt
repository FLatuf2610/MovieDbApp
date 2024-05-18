package com.example.moviedbapp.domain.useCases

import com.example.moviedbapp.data.network.dto.movieDetail.toDomain
import com.example.moviedbapp.data.repository.MoviesRepositoryImpl
import com.example.moviedbapp.presentation.models.Movie
import javax.inject.Inject

class GetMovieDetailUseCase @Inject constructor(private val repository: MoviesRepositoryImpl){

    suspend operator fun invoke(movieId: Int): Movie {
        val movieResponse = repository.getMovieById(movieId)
        return movieResponse.toDomain()
    }

}