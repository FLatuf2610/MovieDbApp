package com.example.moviedbapp.domain.useCases

import com.example.moviedbapp.data.network.local.entities.MovieEntity
import com.example.moviedbapp.data.repository.MoviesRepositoryImpl
import javax.inject.Inject

class GetSavedMovieById @Inject constructor(private val repositoryImpl: MoviesRepositoryImpl){

    suspend operator fun invoke(id: Int): MovieEntity? {
        return repositoryImpl.getSavedMovieById(id)
    }
}