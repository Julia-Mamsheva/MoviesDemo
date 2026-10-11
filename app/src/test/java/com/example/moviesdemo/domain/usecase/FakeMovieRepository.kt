package com.example.moviesdemo.domain.usecase

import com.example.moviesdemo.domain.model.Movie
import com.example.moviesdemo.domain.repository.MovieRepository
import java.io.IOException

class FakeMovieRepository : MovieRepository {

    private var movies = listOf<Movie>()

    // Рубильник: включили — getMovies бросит ошибку (имитация сети/БД)
    var shouldThrow = false

    // Запоминаем аргументы последнего вызова, чтобы проверить их в тесте
    var lastCoverUpdate: Pair<String, String>? = null

    fun setMovies(list: List<Movie>) { movies = list }

    override suspend fun getMovies(): List<Movie> {
        if (shouldThrow) throw IOException("fake failure")
        return movies
    }

    override suspend fun toggleFavorite(movieId: String) {
        movies = movies.map {
            if (it.id == movieId) it.copy(isFavorite = !it.isFavorite) else it
        }
    }

    override suspend fun updateCoverImage(movieId: String, sourceUri: String): String {
        lastCoverUpdate = movieId to sourceUri
        return "file://covers/$movieId.jpg"
    }
}