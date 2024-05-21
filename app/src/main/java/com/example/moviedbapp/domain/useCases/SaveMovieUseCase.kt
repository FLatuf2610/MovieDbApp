package com.example.moviedbapp.domain.useCases

import com.example.moviedbapp.data.repository.MoviesRepositoryImpl
import com.example.moviedbapp.presentation.models.Movie
import com.example.moviedbapp.presentation.models.toEntity
import javax.inject.Inject

class SaveMovieUseCase @Inject constructor(private val repositoryImpl: MoviesRepositoryImpl) {

    suspend operator fun invoke(movie: Movie) {
        val entity = movie.toEntity()
        repositoryImpl.saveMovie(entity)
    }
}