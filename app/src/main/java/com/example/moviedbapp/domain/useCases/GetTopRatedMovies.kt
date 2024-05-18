package com.example.moviedbapp.domain.useCases

import com.example.moviedbapp.data.network.dto.listResponse.toDomain
import com.example.moviedbapp.data.repository.MoviesRepositoryImpl
import javax.inject.Inject

class GetTopRatedMovies @Inject constructor(private val repositoryImpl: MoviesRepositoryImpl) {
    suspend operator fun invoke(page: Int) = repositoryImpl.getTopRatedMovies(page).toDomain()
}