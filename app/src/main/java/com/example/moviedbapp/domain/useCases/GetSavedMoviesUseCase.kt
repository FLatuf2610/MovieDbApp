package com.example.moviedbapp.domain.useCases

import com.example.moviedbapp.data.local.entities.toListItem
import com.example.moviedbapp.data.repository.MoviesRepositoryImpl
import com.example.moviedbapp.presentation.models.MovieItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSavedMoviesUseCase @Inject constructor(private val repositoryImpl: MoviesRepositoryImpl) {

    suspend operator fun invoke(): Flow<List<MovieItem>> =
        repositoryImpl.getAllMoviesFromDb().map { list -> list.map { entity -> entity.toListItem() } }
}