package com.example.moviesdemo.data.repository

import android.util.Log
import com.example.moviesdemo.domain.model.Movie
import com.example.moviesdemo.domain.repository.MovieRepository
import kotlinx.coroutines.delay
import javax.inject.Inject


// Реализует интерфейс из Domain. Единственный класс в проекте,
// который знает, ГДЕ реально лежат данные.
class MovieRepositoryImpl @Inject constructor() : MovieRepository {

    // In-memory "база" — для простого опорного проекта достаточно.
    // Когда решите подключить реальную сеть или локальную базу — меняется только этот класс
    private val movies = mutableListOf(
        Movie("1", "Матрица", rating = 8.7, isFavorite = false),
        Movie("2", "Начало", rating = 8.8, isFavorite = false),
        Movie("3", "Интерстеллар", rating = 8.6, isFavorite = true),
    )

    override suspend fun getMovies(): List<Movie> {
        delay(300) // имитация сетевой задержки — почувствовать loading-состояние
        return movies.toList() // копия — чтобы никто снаружи не поменял список напрямую
    }

    override suspend fun toggleFavorite(movieId: String) {
        val index = movies.indexOfFirst { it.id == movieId }
        if (index != -1) {
            Log.d("First", movies[index].title + movies[index].isFavorite.toString())
            movies[index] = movies[index].copy(isFavorite = !movies[index].isFavorite)
            Log.d("Second",movies[index].title + movies[index].isFavorite.toString())

        }
    }
}