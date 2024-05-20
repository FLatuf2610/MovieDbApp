package com.example.moviedbapp.domain.useCases

import com.example.moviedbapp.data.network.local.entities.MovieEntity
import com.example.moviedbapp.data.repository.MoviesRepositoryImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavedMoviesUseCase @Inject constructor(private val repositoryImpl: MoviesRepositoryImpl) {

    suspend operator fun invoke(): Flow<List<MovieEntity>> = repositoryImpl.getAllMoviesFromDb()
}