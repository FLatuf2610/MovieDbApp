package com.example.moviedbapp.domain.useCases

import com.example.moviedbapp.data.network.dto.collection.toDomain
import com.example.moviedbapp.data.repository.MoviesRepositoryImpl
import com.example.moviedbapp.presentation.models.Collection
import javax.inject.Inject

class GetCollectionUseCase @Inject constructor(private val repositoryImpl: MoviesRepositoryImpl){

    suspend operator fun invoke(id:Int): Collection = repositoryImpl.getCollection(id).toDomain()
}