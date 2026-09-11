package com.example.moviesdemo.data.repository

import android.util.Log
import com.example.moviesdemo.data.local.MovieDao
import com.example.moviesdemo.data.local.MovieEntity
import com.example.moviesdemo.data.local.toDomain
import com.example.moviesdemo.domain.model.Movie
import com.example.moviesdemo.domain.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


// data/repository/MovieRepositoryImpl.kt

class MovieRepositoryImpl @Inject constructor(
    private val dao: MovieDao
) : MovieRepository {

    init {
        // "Посев" начальных данных — заменяет старый mutableListOf(...).
        // В реальном приложении на этом месте была бы синхронизация с сетью.
        seedIfEmpty()
    }

    private fun seedIfEmpty() {
        CoroutineScope(Dispatchers.IO).launch {
            if (dao.count() == 0) {
                dao.insertAll(listOf(
                    MovieEntity("1", "Матрица", rating = 8.7, isFavorite = false),
                    MovieEntity("2", "Начало", rating = 8.8, isFavorite = false),
                    MovieEntity("3", "Интерстеллар", rating = 8.6, isFavorite = true),
                ))
            }
        }
    }

    override suspend fun getMovies(): List<Movie> {
        return dao.observeAll().first().map { it.toDomain() }
        // .first() — берём одно текущее значение Flow,
        // интерфейс MovieRepository пока остаётся suspend-функцией, не Flow
    }

    override suspend fun toggleFavorite(movieId: String) {
        val current = dao.observeAll().first().first { it.id == movieId }
        dao.setFavorite(movieId, !current.isFavorite)
    }
}