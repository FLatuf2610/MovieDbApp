package com.example.moviedbapp.domain.useCases

import com.example.moviedbapp.data.network.local.entities.MovieEntity
import com.example.moviedbapp.data.repository.MoviesRepositoryImpl
import javax.inject.Inject

class DeleteMovieUseCase @Inject constructor(private val repositoryImpl: MoviesRepositoryImpl) {

    suspend operator fun invoke(id: Int, title: String, posterPath: String?) {
        val entity = MovieEntity(id = id, title = title, poster_path = posterPath)
        repositoryImpl.deleteMovie(entity)
    }
}