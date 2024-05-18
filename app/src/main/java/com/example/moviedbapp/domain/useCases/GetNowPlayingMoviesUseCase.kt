package com.example.moviedbapp.domain.useCases

import com.example.moviedbapp.data.network.dto.listResponse.toDomain
import com.example.moviedbapp.data.repository.MoviesRepositoryImpl
import com.example.moviedbapp.presentation.models.MovieList
import javax.inject.Inject

class GetNowPlayingMoviesUseCase @Inject constructor(private val repositoryImpl: MoviesRepositoryImpl){
    suspend operator fun invoke(page: Int): MovieList = repositoryImpl.getNowPlayingMovies(page).toDomain()
}