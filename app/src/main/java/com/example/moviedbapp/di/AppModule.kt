package com.example.moviedbapp.di

import com.example.moviedbapp.data.network.apiClient.MovieDbApiClient
import com.example.moviedbapp.utils.AuthInterceptor
import com.example.moviedbapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(Constants.READ_TOKEN))
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiClient(retrofit: Retrofit): MovieDbApiClient {
        return retrofit
            .create(MovieDbApiClient::class.java)
    }

}