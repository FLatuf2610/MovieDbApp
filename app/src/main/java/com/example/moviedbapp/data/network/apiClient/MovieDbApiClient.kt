package com.example.moviedbapp.data.network.apiClient

import com.example.moviedbapp.data.network.dto.collection.CollectionResponseDto
import com.example.moviedbapp.data.network.dto.listResponse.ListResponseDto
import com.example.moviedbapp.data.network.dto.movieDetail.MovieDetailResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieDbApiClient {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int
    ): ListResponseDto

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int
    ): ListResponseDto

    @GET("movie/now_playing")
    suspend fun getNowPlaying(
        @Query("page") page: Int
    ): ListResponseDto

    @GET("movie/{id}/similar")
    suspend fun getRelatedMovies(
        @Path("id") movieId: Int,
        @Query("page") page: Int
    ): ListResponseDto

    @GET("movie/{id}")
    suspend fun getMovieById(
        @Path("id") movieId: Int
    ): MovieDetailResponseDto

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query: String, @Query("page") page: Int
    ): ListResponseDto

    @GET("movie/upcoming")
    suspend fun getIncomingMovies(
        @Query("page") page: Int
    ): ListResponseDto

    @GET("collection/{id}")
    suspend fun getCollection(
        @Path("id") id: Int
    ): CollectionResponseDto

}