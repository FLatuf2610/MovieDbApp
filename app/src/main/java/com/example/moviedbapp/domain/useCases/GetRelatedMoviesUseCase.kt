package com.example.moviedbapp.domain.useCases

import com.example.moviedbapp.data.network.dto.listResponse.toDomain
import com.example.moviedbapp.data.repository.MoviesRepositoryImpl
import com.example.moviedbapp.presentation.models.MovieList
import javax.inject.Inject

class GetRelatedMoviesUseCase @Inject constructor(
    private val repositoryImpl: MoviesRepositoryImpl
) {
    suspend operator fun invoke(movieId: Int, page: Int): MovieList {
        val response = repositoryImpl.getRelatedMovies(movieId, page).toDomain()
        val newList = response.movieList.slice(0..11)
        return response.copy(movieList = newList)
    }
}
