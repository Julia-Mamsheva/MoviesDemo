package com.example.moviesdemo.data.di

import android.content.Context
import androidx.room3.Room
import com.example.moviesdemo.data.local.MovieDao
import com.example.moviesdemo.data.local.MoviesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton // база данных ОБЯЗАНА быть одна на всё приложение
    fun provideDatabase(@ApplicationContext context: Context): MoviesDatabase {
        return Room.databaseBuilder(
            context,
            MoviesDatabase::class.java,
            "movies.db"
        ).build()
    }

    @Provides
    fun provideMovieDao(database: MoviesDatabase): MovieDao = database.movieDao()
}