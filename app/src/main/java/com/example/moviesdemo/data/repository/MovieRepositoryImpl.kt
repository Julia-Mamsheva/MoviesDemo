package com.example.moviesdemo.data.repository

import android.util.Log
import com.example.moviesdemo.data.local.MovieDao
import com.example.moviesdemo.data.local.MovieEntity
import com.example.moviesdemo.data.local.MovieLocalDataSource
import com.example.moviesdemo.data.local.toDomain
import com.example.moviesdemo.data.remote.MovieRemoteDataSource
import com.example.moviesdemo.domain.model.Movie
import com.example.moviesdemo.domain.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

// data/repository/MovieRepositoryImpl.kt

class MovieRepositoryImpl @Inject constructor(
    private val remote: MovieRemoteDataSource,
    private val local: MovieLocalDataSource
) : MovieRepository {

    override suspend fun getMovies(): List<Movie> {
        return try {
            // Сначала пробуем сеть — свежие данные
            val remoteMovies = remote.fetchPopularMovies().map { dto ->
                Movie(
                    id = dto.id.toString(),
                    title = dto.title,
                    rating = dto.voteAverage,
                    isFavorite = false // избранное — локальная информация, сеть про него не знает
                )
            }
            local.cacheMovies(remoteMovies) // обновляем кэш свежими данными
            local.getCachedMovies() // читаем обратно из кэша — там уже сохранены isFavorite=true для тех, что были избранными
        } catch (e: IOException) {
            // Нет сети — откатываемся на то, что уже закэшировано (offline-first)
            local.getCachedMovies()
        }
    }

    override suspend fun toggleFavorite(movieId: String) {
        val current = local.getCachedMovies().first { it.id == movieId }
        local.setFavorite(movieId, !current.isFavorite)
    }
}