package com.example.moviedbapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moviedbapp.data.local.dao.MoviesDao
import com.example.moviedbapp.data.local.entities.MovieEntity

@Database(entities = [MovieEntity::class], version = 1)
abstract class MoviesDatabase: RoomDatabase() {

    abstract fun getDao(): MoviesDao
}