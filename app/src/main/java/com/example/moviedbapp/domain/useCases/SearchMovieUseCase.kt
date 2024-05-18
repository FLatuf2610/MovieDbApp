package com.example.moviedbapp.domain.useCases

import com.example.moviedbapp.data.network.dto.listResponse.toDomain
import com.example.moviedbapp.data.repository.MoviesRepositoryImpl
import com.example.moviedbapp.presentation.models.MovieList
import javax.inject.Inject

class SearchMovieUseCase @Inject constructor(private val repositoryImpl: MoviesRepositoryImpl) {
    suspend operator fun invoke(query: String, page: Int): MovieList =
        repositoryImpl.searchMovie(query, page).toDomain()
}