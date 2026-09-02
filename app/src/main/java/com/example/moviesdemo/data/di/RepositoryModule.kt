package com.example.moviesdemo.data.di

import com.example.moviesdemo.data.repository.MovieRepositoryImpl
import com.example.moviesdemo.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class) // один репозиторий на всё приложение
abstract class RepositoryModule {

    @Binds
    abstract fun bindMovieRepository(
        impl: MovieRepositoryImpl
    ): MovieRepository
    // ↑ "Когда кто-то попросит MovieRepository — подставляй MovieRepositoryImpl"
}