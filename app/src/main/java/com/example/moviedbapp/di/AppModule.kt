package com.example.moviedbapp.di

import android.content.Context
import androidx.room.Room
import com.example.moviedbapp.data.network.apiClient.MovieDbApiClient
import com.example.moviedbapp.data.local.MoviesDatabase
import com.example.moviedbapp.data.local.dao.MoviesDao
import com.example.moviedbapp.data.network.interceptor.AuthInterceptor
import com.example.moviedbapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(authInterceptor: AuthInterceptor): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
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

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MoviesDatabase {
        return Room.databaseBuilder(context, MoviesDatabase::class.java, "movies_db")
            .build()
    }

    @Singleton
    @Provides
    fun provideDao(db: MoviesDatabase): MoviesDao {
        return db.getDao()
    }

}